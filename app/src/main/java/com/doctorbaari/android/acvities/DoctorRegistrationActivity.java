package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
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

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DoctorRegistrationActivity extends AppCompatActivity {

    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etMedicalCollegeName)
    EditText etMedicalCollegeName;
    @BindView(R.id.etContactNo)
    EditText etContactno;


    @BindView(R.id.etRegistrationNo)
    EditText etRegNo;

    ProgressDialog dialog;
    AsyncHttpClient client;
    final static int APP_REQUEST_CODE = 55;
    String phoneNumber = "";
    String workingPlaceName = "";
    String workingPlaceLat = "";
    String workingPlaceLon = "";
    ArrayList<String> degrees;
    String email = "Not Available";
    String profileLink = "Not Available";
    String imageLink = "Not Available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Doctor Registration");
        registerPlaceFragment();
        degrees = new ArrayList<>();
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting with Doctor Baari server...");

        verifyNumber();



    }


    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void goSignup(View v) {

        String fullName = etFullName.getText().toString().trim();
        String medicalCollege = etMedicalCollegeName.getText().toString();
        String contactNo = etContactno.getText().toString().trim();
        String dateOfBirth = "Not Available";
        String workLocation = workingPlaceName;
        String regNo = etRegNo.getText().toString().trim();

        String degree;
        JSONArray array = new JSONArray(degrees);
        degree = array.toString();
        if(degrees.isEmpty())
        {
            Log.d("--------", "Degrees empty");
        }
        if(fullName.isEmpty())
        {
            Log.d("--------", "full name empty");
        }
        if(medicalCollege.isEmpty())
        {
            Log.d("--------", "Medical College Empty");
        }
        if(contactNo.isEmpty())
        {
            Log.d("-------", "contacty");
        }
        if(workLocation.isEmpty())
        {
            Log.d("---------", "work");
        }
        if(regNo.isEmpty())
        {
            Log.d("-------", "reg no");
        }
        if (degrees.isEmpty() || fullName.isEmpty() ||
                medicalCollege.isEmpty() || contactNo.isEmpty() || workLocation.isEmpty() ||
                regNo.isEmpty() || workingPlaceName.isEmpty()) {
            showToast("All field are required");

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
        params.put("type", "doctor");
        params.put("email", email);
        params.put("link", profileLink);
        params.put("picture_url", imageLink);
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
                    startActivity(new Intent(DoctorRegistrationActivity.this, LoginActivity.class));
                    finish();

                } else {
                    DBHelper.setUserId(DoctorRegistrationActivity.this, response);
                    DBHelper.setSignedInStatus(DoctorRegistrationActivity.this, true);
                    showToast("Account created successfully");
                    startActivity(new Intent(DoctorRegistrationActivity.this, ConnectWithFacebookActivity.class));


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
                        showToast("Erro gettign number");
                        Log.d("------------", error.toString());
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
                        AccountKitActivity.ResponseType.TOKEN).setDefaultCountryCode("+880"); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);

    }


    @Override
    public void onBackPressed() {

        if(SideNToolbarController.isDrawerOpen())
            SideNToolbarController.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void registerPlaceFragment() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("BD")
                .build();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.etWorkLocation);
        autocompleteFragment.setHint("Currently working location");
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

    public void degreeChecked(View v) {
        CheckBox cb = (CheckBox) v;

        if (cb.isChecked()) {

            degrees.add(cb.getText().toString());
        } else {
            degrees.remove(cb.getText().toString());
        }

    }
}
