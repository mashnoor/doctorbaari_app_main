package com.doctorbaari.android.acvities;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PostSubstituteActivity extends AppCompatActivity {

    @BindView(R.id.etInstituteName)
    EditText etInstituteName;

    @BindView(R.id.etDetails)
    EditText etDetails;

    @BindView(R.id.tvFromDate)
    TextView tvDateFrom;
    @BindView(R.id.tvToDate)
    TextView tvToDate;

    AsyncHttpClient client;
    ProgressDialog dialog;

    String placename = "", placelat = "", placelon = "";

    ArrayList<String> degrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_substitue);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Post For Substitute");
        registerPlaceFragment();
        degrees = new ArrayList<>();

        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to Doctor Baari server...");
        final int[] which = {0};


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



                if (which[0] == 0)
                tvDateFrom.setText(day + "-" + month + "-" + year);

                else
                    tvToDate.setText(day + "-" + month + "-" + year);
            }

        };

        tvDateFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                which[0] = 0;
                new DatePickerDialog(PostSubstituteActivity.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                which[0] = 1;
                new DatePickerDialog(PostSubstituteActivity.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void goPost(View v) {
        String instituteName = etInstituteName.getText().toString().trim();

        String details = etDetails.getText().toString();

        String date_to = tvToDate.getText().toString();
        String date_from = tvDateFrom.getText().toString();
        JSONArray degreesJson = new JSONArray(degrees);
        String degree = degreesJson.toString();
        RequestParams params = new RequestParams();
        if (date_to.isEmpty() || date_from.isEmpty() || instituteName.isEmpty() || details.isEmpty() || placename.isEmpty() || degrees.isEmpty()) {
            HelperFunc.showToast(PostSubstituteActivity.this, "All fields are required");
            return;
        }

        params.put("date_to", date_to);
        params.put("date_from", date_from);
        params.put("institute", instituteName);
        params.put("details", details);
        params.put("place", placename);
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        params.put("userid", DBHelper.getUserid(PostSubstituteActivity.this));
        params.put("degree", degree);

        client.post(Constants.POST_SUB_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                dialog.dismiss();
                showToast("Successfully Posted");
                viewAvailableDoctors();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                dialog.dismiss();
                Log.d("--------", new String(responseBody));
            }
        });


    }

    private void viewAvailableDoctors() {


        String date_to = tvToDate.getText().toString();
        String date_from = tvDateFrom.getText().toString();
        JSONArray degreesJson = new JSONArray(degrees);
        String degree = degreesJson.toString();
        Intent i = new Intent(this, ViewAvailableDoctorsActivity.class);
        i.putExtra("fromdate", date_from);
        i.putExtra("todate", date_to);
        i.putExtra("degrees", degree);
        i.putExtra("placelat", placelat);
        i.putExtra("placelon", placelon);
        i.putExtra("type", "sub");
        startActivity(i);
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
                placename = place.getName().toString();
                placelat = String.valueOf(place.getLatLng().latitude);
                placelon = String.valueOf(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Logger.d(status);
            }
        });

    }

    public void goPostedSubJobs(View v) {
        Intent i = new Intent(this, HistoryActivity.class);
        i.putExtra("type", "sub");
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
