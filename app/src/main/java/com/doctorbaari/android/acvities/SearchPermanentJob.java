package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;


import com.doctorbaari.android.R;

import com.doctorbaari.android.adapters.JobAdapter;
import com.doctorbaari.android.models.Job;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.SideNToolbarController;
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

public class SearchPermanentJob extends AppCompatActivity {

    @BindView(R.id.etDeadline)
    EditText etDeadline;

    @BindView(R.id.spnrDegree)
    Spinner spnrPreferredDegree;

    @BindView(R.id.lvPermanentJobSearchResult)
    ListView lvPermanentJobSearch;

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
    }

    public void searchPermanent(View v)
    {
        String deadline = etDeadline.getText().toString();
        String degree = spnrPreferredDegree.getSelectedItem().toString();

        RequestParams params = new RequestParams();
        params.put("deadline", deadline);
        params.put("degree", degree);
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
                Job[] posts = Geson.g().fromJson(response, Job[].class);
                adapter = new JobAdapter(SearchPermanentJob.this, posts);
                lvPermanentJobSearch.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                Logger.d(new String(responseBody));

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
