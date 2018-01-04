package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.DoctorAdapter;
import com.doctorbaari.android.models.DoctorSub;
import com.doctorbaari.android.utils.Constants;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class InquiryActivity extends AppCompatActivity {


    String placename = "", placelat = "", placelon = "";

    @BindView(R.id.lvInquiry)
    ListView lvInquiry;

    ProgressDialog dialog;
    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        registerPlaceFragment();
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Hospital Inquiry");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");
        client = new AsyncHttpClient();

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

    public void goInquiry(View v) {
        if (placename.isEmpty()) {
            HelperFunc.showToast(InquiryActivity.this, "All fields must be filled!");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        client.post(Constants.MAKE_INQUIRY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                DoctorSub[] doctorSubs = Geson.g().fromJson(response, DoctorSub[].class);
                DoctorAdapter adapter = new DoctorAdapter(InquiryActivity.this, doctorSubs);
                lvInquiry.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                dialog.dismiss();
                HelperFunc.showToast(InquiryActivity.this, "Something went wrong!");
            }
        });


    }


}
