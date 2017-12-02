package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.AvaibilityListAdapter;
import com.doctorbaari.android.models.Avaibility;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class AvaibilityListActivity extends AppCompatActivity {

    @BindView(R.id.etAvaiblabeFromDate)
    EditText etAvailableFromDate;

    @BindView(R.id.etAvailableToDate)
    EditText getEtAvailableTodate;

    @BindView(R.id.lvAvaibilityList)
    ListView lvAvaibilityList;

    @BindView(R.id.swtchAvailable)
    Switch swtchAvailable;

    String placename = "", placelat = "", placelon = "";

    ProgressDialog dialog;
    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaibility_list);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "My Avaibility Schedule");
        registerPlaceFragment();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");
        client = new AsyncHttpClient();
        Logger.addLogAdapter(new AndroidLogAdapter());
        loadAvaibilityList();

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void addAvaibilityStatus(View v) {
        String fromdate = etAvailableFromDate.getText().toString();
        String todate = getEtAvailableTodate.getText().toString();
        RequestParams params = new RequestParams();
        params.put("fromdate", fromdate);
        params.put("todate", todate);
        params.put("place", placename);
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        params.put("available", String.valueOf(swtchAvailable.isChecked()));
        params.put("userid", DBHelper.getUserid(AvaibilityListActivity.this));
        client.post(Constants.ADD_TO_AVAIBILITY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                dialog.dismiss();
                showToast("Successfully added to avaibility list");
                loadAvaibilityList();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                showToast("Some error occured");
                Logger.d(new String(responseBody));


            }
        });
    }

    private void loadAvaibilityList()
    {
        RequestParams params = new RequestParams();
        params.put("userid", DBHelper.getUserid(AvaibilityListActivity.this));
        client.post(Constants.GET_AVAIBILITY_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Avaibility[] avaibilities = Geson.g().fromJson(response, Avaibility[].class);
                AvaibilityListAdapter adapter = new AvaibilityListAdapter(AvaibilityListActivity.this, avaibilities);
                lvAvaibilityList.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showToast("Something went wrong");
                dialog.dismiss();
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
