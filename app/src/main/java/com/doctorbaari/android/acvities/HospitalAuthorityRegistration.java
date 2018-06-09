package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.HelperFunc;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HospitalAuthorityRegistration extends AppCompatActivity {

    @BindView(R.id.etInstituteName)
    EditText etInstitutiteName;

    @BindView(R.id.etContactNo)
    EditText etContactno;

    @BindView(R.id.etRepresentativeName)
    EditText etRepresentativeName;

    @BindView(R.id.etDesignation)
    EditText etDesignation;


    ProgressDialog dialog;
    AsyncHttpClient client;
    final static int APP_REQUEST_CODE = 55;
    String phoneNumber = "";
    String workingPlaceName = "";
    String workingPlaceLat = "";
    String workingPlaceLon = "";


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_authority_registration);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Hospital Authority Registration");
        registerPlaceFragment();
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting with Doctor Baari server...");

        verifyNumber();


    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void goSignup(View v) {

        String fullName = etRepresentativeName.getText().toString().trim();
        String medicalCollege = etInstitutiteName.getText().toString().trim();
        String contactNo = etContactno.getText().toString().trim();
        String dateOfBirth = "Not Available";
        String workLocation = workingPlaceName;
        String regNo = "Not Available";
        String degree = etDesignation.getText().toString();
        if (fullName.isEmpty()) {
            etInstitutiteName.setError("Full name can't be empty!");
            return;
        }

        if (contactNo.isEmpty()) {
            etContactno.setError("Contact no can't be empty");
            return;
        }

        if (workLocation.isEmpty()) {
            showToast("Please select your working location");
            return;
        }


        RequestParams params = new RequestParams();
        params.put("username", fullName);
        params.put("medicalcollege", medicalCollege);
        params.put("regno", regNo);
        params.put("contact", contactNo);
        params.put("degree", degree);
        params.put("dateofbirth", dateOfBirth);
        params.put("place", workLocation);
        params.put("placelat", workingPlaceLat);
        params.put("placelon", workingPlaceLon);
        params.put("type", "hospital");
        params.put("token", FirebaseInstanceId.getInstance().getToken());
        client.post(Constants.SIGNUP_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                String response = new String(responseBody);
                if (response.equals("occupied")) {
                    showToast("You created an account already. Please login with phone");
                    startActivity(new Intent(HospitalAuthorityRegistration.this, LoginActivity.class));
                    finish();

                } else {
                    DBHelper.setUserId(HospitalAuthorityRegistration.this, response);
                    DBHelper.setSignedInStatus(HospitalAuthorityRegistration.this, true);
                    showToast("Account created successfully");
                    startActivity(new Intent(HospitalAuthorityRegistration.this, HomeActitvity.class));
                    finish();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                //Log.d("--------", new String(responseBody));
                dialog.dismiss();

                showToast("Some error occured");
            }
        });
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null) {
                showToast("Something went wrong! Try again later");
                finish();


            } else if (loginResult.wasCancelled()) {

                finish();

            } else {


                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        PhoneNumber phn = account.getPhoneNumber();


                        phoneNumber = phn.toString();
                        etContactno.setText(phoneNumber);


                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // Handle Error
                        showToast("Error getting number");
                    }
                });


            }

        }

    }


    private void verifyNumber() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN).setDefaultCountryCode("+880").setReadPhoneStateEnabled(false); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);

    }

    private void registerPlaceFragment() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("BD")
                .build();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.etWorkLocation);
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                workingPlaceName = place.getName().toString();
                workingPlaceLat = String.valueOf(place.getLatLng().latitude);
                workingPlaceLon = String.valueOf(place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.

            }
        });

    }

    public void goTermsAndConditions(View v)
    {
        HelperFunc.openUrlInBrowser(this, "https://doctorbaari.com/#menu#terms");

    }


}
