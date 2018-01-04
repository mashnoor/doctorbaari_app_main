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
import android.widget.Switch;
import android.widget.TextView;

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
import com.orhanobut.logger.Logger;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchSubstituteJobs extends AppCompatActivity {


    AsyncHttpClient client;


    ProgressDialog dialog;


    String placename = "", placelat = "", placelon = "";
    @BindView(R.id.etFromDate)
    EditText etFromDate;
    @BindView(R.id.etToDate)
    EditText etToDate;

    int which;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_substitute_jobs);
        ButterKnife.bind(this);
        registerPlaceFragment();
        registerFuckingCalenderListener();
        SideNToolbarController.attach(this, "Search Substitute Jobs");
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");

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

                if (which == 0)
                    etFromDate.setText(year + "-" + month + "-" + day);

                else
                    etToDate.setText(year + "-" + month + "-" + day);
            }

        };

        etFromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                which = 0;
                new DatePickerDialog(SearchSubstituteJobs.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        etToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                which = 1;
                new DatePickerDialog(SearchSubstituteJobs.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


    }

    public void saveAndSearch(View v) {
        String fromdate = etFromDate.getText().toString();
        String todate = etToDate.getText().toString();
        if (fromdate.isEmpty() || todate.isEmpty() || placename.isEmpty()) {
            HelperFunc.showToast(SearchSubstituteJobs.this, "All fields must be filled");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("fromdate", fromdate);
        params.put("todate", todate);
        params.put("place", placename);
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        params.put("type", "sub");
        params.put("available", "1");
        params.put("userid", DBHelper.getUserid(SearchSubstituteJobs.this));
        client.post(Constants.ADD_TO_AVAIBILITY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                dialog.dismiss();
                HelperFunc.showToast(SearchSubstituteJobs.this, "Successfully added to avaibility list");
                searchSubstituteJobs();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                HelperFunc.showToast(SearchSubstituteJobs.this, "Something went wrong");
                Logger.d(new String(responseBody));


            }
        });
    }


    public void searchSubstituteJobs() {
        String fromdate = etFromDate.getText().toString();
        String todate = etToDate.getText().toString();
        if (fromdate.isEmpty() || todate.isEmpty() || placename.isEmpty()) {
            HelperFunc.showToast(SearchSubstituteJobs.this, "All fields must be filled");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("fromdate", fromdate);

        params.put("todate", todate);
        params.put("place", placename);
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        client.post(Constants.SEARCH_SUBSTITUTE_JOBS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                String response = new String(responseBody);
                Intent i = new Intent(SearchSubstituteJobs.this, SubstituteJobSearchResult.class);
                i.putExtra("response", response);
                startActivity(i);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                Logger.d(new String(responseBody));
                HelperFunc.showToast(SearchSubstituteJobs.this, "Something went wrong");

            }
        });
    }

}
