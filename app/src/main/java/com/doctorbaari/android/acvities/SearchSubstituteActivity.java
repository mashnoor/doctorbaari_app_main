package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.DoctorAdapter;
import com.doctorbaari.android.models.DoctorSub;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchSubstituteActivity extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_substitute);
        ButterKnife.bind(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        registerPlaceFragment();
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting with server...");
    }

    public void searchSubstitute(View v)
    {
        RequestParams params = new RequestParams();
        String fromDate = tvFromDate.getText().toString();
        String toDate = tvToDate.getText().toString();
        String preferredDegree = spnrDegree.getSelectedItem().toString();
        params.put("fromdate", fromDate);
        params.put("todate", toDate);
        params.put("place", placename);
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        params.put("degree", preferredDegree);
        params.put("userid", DBHelper.getUserid(SearchSubstituteActivity.this));
        client.post(Constants.SEARCH_SUB, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                DoctorSub[] subs = Geson.g().fromJson(response, DoctorSub[].class);
                DoctorAdapter adapter = new DoctorAdapter(SearchSubstituteActivity.this, subs);
                lvSubstitute.setAdapter(adapter);
                dialog.dismiss();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Logger.d(new String(responseBody));
                dialog.dismiss();

            }
        });



    }


    private void registerPlaceFragment() {

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


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
