package com.doctorbaari.android.acvities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.doctorbaari.android.R;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class SearchPermanentJob extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_permanent_job);
        Logger.addLogAdapter(new AndroidLogAdapter());
        registerPlaceFragment();
    }

    private void registerPlaceFragment() {

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Logger.d(place.getName());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Logger.d(status);
            }
        });

    }
}
