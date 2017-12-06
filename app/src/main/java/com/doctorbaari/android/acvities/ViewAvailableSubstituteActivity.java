package com.doctorbaari.android.acvities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.DoctorAdapter;
import com.doctorbaari.android.models.DoctorSub;
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

public class ViewAvailableSubstituteActivity extends AppCompatActivity {


    String placename = "", placelat = "", placelon = "";

    @BindView(R.id.tvFromDate)
    TextView tvFromDate;

    @BindView(R.id.tvToDate)
    TextView tvToDate;

    @BindView(R.id.spnrDegree)
    Spinner spnrDegree;

    @BindView(R.id.lvSubstitute)
    ListView lvSubstitute;

    AsyncHttpClient client;

    ProgressDialog dialog;
    int which;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_available_substitute);
        ButterKnife.bind(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        SideNToolbarController.attach(this, "View Available Substitutes");
        registerPlaceFragment();
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting with server...");
        registerFuckingCalenderListener();
    }

    private void registerFuckingCalenderListener()
    {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                if (which == 0)
                    tvFromDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);

                else
                    tvToDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }

        };

        tvFromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                which = 0;
                new DatePickerDialog(ViewAvailableSubstituteActivity.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                which = 1;
                new DatePickerDialog(ViewAvailableSubstituteActivity.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    public void searchSubstitute(View v)
    {
        RequestParams params = new RequestParams();
        String fromDate = tvFromDate.getText().toString();
        String toDate = tvToDate.getText().toString();
        String preferredDegree = spnrDegree.getSelectedItem().toString();
        if(fromDate.isEmpty() || toDate.isEmpty() || placename.isEmpty() || placelat.isEmpty() || placelon.isEmpty() || preferredDegree.isEmpty())
        {
            HelperFunc.showToast(ViewAvailableSubstituteActivity.this, "All fields must be filled");
            return;
        }
        params.put("fromdate", fromDate);
        params.put("todate", toDate);
        params.put("place", placename);
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        params.put("degree", preferredDegree);
        params.put("userid", DBHelper.getUserid(ViewAvailableSubstituteActivity.this));
        client.post(Constants.SEARCH_AVAILABLE_SUB, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Logger.d(response);
                DoctorSub[] subs = Geson.g().fromJson(response, DoctorSub[].class);
                DoctorAdapter adapter = new DoctorAdapter(ViewAvailableSubstituteActivity.this, subs);
                lvSubstitute.setAdapter(adapter);
                dialog.dismiss();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Logger.d(new String(responseBody));
                dialog.dismiss();
                HelperFunc.showToast(ViewAvailableSubstituteActivity.this, "Something went wrong");

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
}
