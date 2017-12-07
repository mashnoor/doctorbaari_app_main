package com.doctorbaari.android.acvities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;


import com.doctorbaari.android.R;

import com.doctorbaari.android.adapters.JobAdapter;
import com.doctorbaari.android.models.Job;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
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
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchPermanentJob extends AppCompatActivity {

    @BindView(R.id.etFromDate)
    EditText etFromDate;

    @BindView(R.id.lvPermanentJobSearchResult)
    ListView lvPermanentJobSearch;

    @BindView(R.id.swtchAvailable)
    Switch swtchAvailable;

    AsyncHttpClient client;

    JobAdapter adapter;

    ProgressDialog dialog;


    String placename = "", placelat = "", placelon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_permanent_job);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Search Permanent Job");
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to Server...");
        Logger.addLogAdapter(new AndroidLogAdapter());
        registerPlaceFragment();
        registerFuckingCalenderListener();
    }

    public void searchPermanent(View v) {
        String fromDate = etFromDate.getText().toString();
        if (fromDate.isEmpty() || placename.isEmpty()) {
            HelperFunc.showToast(SearchPermanentJob.this, "All fields must be filled");
            return;
        }

        RequestParams params = new RequestParams();
        params.put("fromdate", fromDate);
        params.put("userid", DBHelper.getUserid(SearchPermanentJob.this));
        params.put("place", placename);
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        client.post(Constants.SEARCH_PERMANENT_JOB, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                String response = new String(responseBody);
                Intent i = new Intent(SearchPermanentJob.this, SubstituteJobSearchResult.class);
                i.putExtra("response", response);
                startActivity(i);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                Logger.d(new String(responseBody));

            }
        });
    }

    private void registerFuckingCalenderListener() {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                etFromDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);


            }

        };
        etFromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                new DatePickerDialog(SearchPermanentJob.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

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

    public void addToAvailability(View v) {
        String fromdate = etFromDate.getText().toString();
        String todate = "Not Available";
        if (fromdate.isEmpty() || todate.isEmpty() || placename.isEmpty()) {
            HelperFunc.showToast(SearchPermanentJob.this, "All fields must be filled");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("fromdate", fromdate);
        params.put("todate", todate);
        params.put("place", placename);
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        params.put("type", "per");
        params.put("available", String.valueOf(swtchAvailable.isChecked() ? 1 : 0));
        params.put("userid", DBHelper.getUserid(SearchPermanentJob.this));
        client.post(Constants.ADD_TO_AVAIBILITY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                dialog.dismiss();
                HelperFunc.showToast(SearchPermanentJob.this, "Successfully added to availability list");
                searchPermanent(null);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                HelperFunc.showToast(SearchPermanentJob.this, "Something went wrong");
                Logger.d(new String(responseBody));


            }
        });
    }

}
