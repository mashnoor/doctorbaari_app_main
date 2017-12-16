package com.doctorbaari.android.acvities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.HelperFunc;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class PostPermanentJobActivity extends AppCompatActivity {

    @BindView(R.id.etInstitution)
    EditText etInstitution;
    @BindView(R.id.etStartingFrom)
    EditText etStatingFrom;


    @BindView(R.id.etDetails)
    EditText etDetails;
    @BindView(R.id.ivPermanentJobImage)
    ImageView ivPermanentJobImage;

    AsyncHttpClient client;
    ProgressDialog dialog;

    String placeName;
    String placeLat;
    String placeLon;

    File imgfile = null;

    ArrayList<String> degrees;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_permanent_job);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Post Permanent Job");
        registerPlaceFragment();
        degrees = new ArrayList<>();
        Logger.d(new AndroidLogAdapter());
        registerFuckingCalenderListener();


        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Getting data from server...");
    }

    private void registerFuckingCalenderListener() {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                String month = String.valueOf(monthOfYear + 1);
                String day = String.valueOf(dayOfMonth);
                if (month.length() == 1)
                    month = "0" + month;
                // TODO Auto-generated method stub
                if (day.length() == 1)
                    day = "0" + day;


                etStatingFrom.setText(year + "-" + month + "-" + day);


            }

        };
        etStatingFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new DatePickerDialog(PostPermanentJobActivity.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void goPost(View v) {
        String institution = etInstitution.getText().toString();
        String startingfrom = etStatingFrom.getText().toString();
        String placename = placeName;
        JSONArray degreesJson = new JSONArray(degrees);
        String degree = degreesJson.toString();
        String details = etDetails.getText().toString();
        if (startingfrom.isEmpty() || placename.isEmpty() || institution.isEmpty() || details.isEmpty() || degrees.isEmpty()) {
            HelperFunc.showToast(PostPermanentJobActivity.this, "All fields are required");
        }

        RequestParams params = new RequestParams();
        params.put("institute", institution);
        params.put("place", placename);
        params.put("details", details);
        params.put("degree", degree);
        params.put("deadline", startingfrom);
        params.put("placelat", placeLat);
        params.put("placelon", placeLon);
        if (imgfile != null) {
            try {
                params.put("imagefile", imgfile);
            } catch (Exception e) {

            }

        }


        params.put("userid", DBHelper.getUserid(PostPermanentJobActivity.this));


        client.post(Constants.POST_PERMANENT_JOB, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();

                showToast("Successfully posted permanent job");
                viewAvailableDoctors();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                showToast("Something went wrong. Try again later");
                Logger.d(error.getMessage());
                Logger.d(new String(responseBody));

            }
        });

    }

    private void viewAvailableDoctors() {


        String date_to = etStatingFrom.getText().toString();
        String date_from = "Not Available";
        JSONArray degreesArray = new JSONArray(degrees);
        String degree = degreesArray.toString();
        Intent i = new Intent(this, ViewAvailableDoctorsActivity.class);
        i.putExtra("fromdate", date_from);
        i.putExtra("todate", date_to);
        i.putExtra("degrees", degree);
        i.putExtra("placelat", placeLat);
        i.putExtra("placelon", placeLon);
        i.putExtra("type", "per");
        startActivity(i);
    }

    public void selectImage(View v) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {

                    EasyImage.openGallery(PostPermanentJobActivity.this, EasyImage.REQ_PICK_PICTURE_FROM_GALLERY);
                }


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                HelperFunc.showToast(PostPermanentJobActivity.this, "Something went wrong");
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                imgfile = imageFile;
                Glide.with(PostPermanentJobActivity.this).load(imageFile.getAbsolutePath()).into(ivPermanentJobImage);

            }


        });
    }

    private void registerPlaceFragment() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("BD")
                .build();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setFilter(typeFilter);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Logger.d(place.getName());
                placeName = place.getName().toString();
                placeLat = String.valueOf(place.getLatLng().latitude);
                placeLon = String.valueOf(place.getLatLng().longitude);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Logger.d(status);
            }
        });

    }

    public void goPostedPermanentJobs(View v) {
        Intent i = new Intent(this, HistoryActivity.class);
        i.putExtra("type", "per");
        startActivity(i);
    }

    public void degreeChecked(View v) {
        CheckBox cb = (CheckBox) v;

        if (cb.isChecked()) {
            Logger.d(cb.getText().toString());
            degrees.add(cb.getText().toString());
        } else {
            degrees.remove(cb.getText().toString());
        }

    }
}
