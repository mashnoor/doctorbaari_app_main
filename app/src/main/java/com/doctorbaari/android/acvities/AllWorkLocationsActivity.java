package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.WorkLoacationAdapter;
import com.doctorbaari.android.models.WorkLocation;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.fabric.sdk.android.Fabric;

public class AllWorkLocationsActivity extends AppCompatActivity {

    String workingPlaceName = "", workingPlaceLat = "", workingPlaceLon = "";

    ProgressDialog dialog;

    @BindView(R.id.lvWorkLocations)
    ListView lvWorkLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_work_locations);

        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "My Work Locations");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading. Please wait...");
        registerPlaceFragment();
        loadWorkLocations();
    }

    private void loadWorkLocations() {

        RequestParams params = new RequestParams("userid", DBHelper.getUserid(this));

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.GET_ALL_WORK_LOCATIONS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                WorkLocation[] locations = Geson.g().fromJson(response, WorkLocation[].class);
                WorkLoacationAdapter adapter = new WorkLoacationAdapter(AllWorkLocationsActivity.this, locations);
                lvWorkLocations.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                HelperFunc.showToast(AllWorkLocationsActivity.this, "Something went wrong");
                dialog.dismiss();

            }
        });
    }

    public void goSaveAndUpdateLocation(View v) {
        if (workingPlaceName.isEmpty()) {
            HelperFunc.showToast(this, "Select a place");

        } else {
            RequestParams params = new RequestParams();
            params.put("place", workingPlaceName);
            params.put("placelat", workingPlaceLat);
            params.put("placelon", workingPlaceLon);
            params.put("userid", DBHelper.getUserid(this));
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(Constants.ADD_AND_UPDATE_LOCATION, params, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    dialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    HelperFunc.showToast(AllWorkLocationsActivity.this, "Successfully Added");
                    dialog.dismiss();
                    recreate();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    HelperFunc.showToast(AllWorkLocationsActivity.this, "Something went wrong");
                    dialog.dismiss();

                }
            });
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        SideNToolbarController.closeDrawer();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onBackPressed() {

        if(SideNToolbarController.isDrawerOpen())
            SideNToolbarController.closeDrawer();
        else
            super.onBackPressed();
    }

    private void registerPlaceFragment() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("BD")
                .build();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.etWorkLocation);
        autocompleteFragment.setHint("Add New Workplace");
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Logger.d(place.getName());
                workingPlaceName = place.getName().toString();
                workingPlaceLat = String.valueOf(place.getLatLng().latitude);
                workingPlaceLon = String.valueOf(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Logger.d(status);
            }
        });

    }
}
