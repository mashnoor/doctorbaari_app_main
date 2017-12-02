package com.doctorbaari.android.acvities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PostPermanentJobActivity extends AppCompatActivity {

    @BindView(R.id.etInstitution)
    EditText etInstitution;
    @BindView(R.id.etStartingFrom)
    EditText etStatingFrom;

    @BindView(R.id.spnrDegree)
    Spinner spnrDegree;
    @BindView(R.id.etDetails)
    EditText etDetails;

    AsyncHttpClient client;
    ProgressDialog dialog;

    String placeName;
    String placeLat;
    String placeLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_permanent_job);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Post Permanent Job");
        registerPlaceFragment();
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

                etStatingFrom.setText(year + "-" + monthOfYear + "-" + dayOfMonth);


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
        String degree = spnrDegree.getSelectedItem().toString();
        String details = etDetails.getText().toString();

        RequestParams params = new RequestParams();
        params.put("institute", institution);
        params.put("place", placename);
        params.put("details", details);
        params.put("degree", degree);
        params.put("deadline", startingfrom);
        params.put("placelat", placeLat);
        params.put("placelon", placeLon);
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
}
