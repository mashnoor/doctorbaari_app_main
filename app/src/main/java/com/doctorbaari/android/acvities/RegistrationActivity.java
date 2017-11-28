package com.doctorbaari.android.acvities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
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

public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etMedicalCollege)
    EditText etMedicalCollege;
    @BindView(R.id.etContactNo)
    EditText etContactno;
    @BindView(R.id.etDateOfBirth)
    EditText etdateOfBirth;

    @BindView(R.id.etRegistrationNo)
    EditText etRegNo;

    ProgressDialog dialog;
    AsyncHttpClient client;
    final static int APP_REQUEST_CODE = 55;
    String phoneNumber = "";
    String workingPlaceName = "";
    String workingPlaceLat = "";
    String workingPlaceLon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        registerPlaceFragment();
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting with Doctor Baari server...");
        Logger.addLogAdapter(new AndroidLogAdapter());
        verifyNumber();
        getCollegeList();
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                etdateOfBirth.setText(year + "-" + monthOfYear + "-" + dayOfMonth);

            }

        };

        etdateOfBirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegistrationActivity.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void getCollegeList()
    {
        client.get(Constants.GET_COLLEGE_LIST, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void goSignup(View v) {

        String fullName = etFullName.getText().toString().trim();
        String medicalCollege = etMedicalCollege.getText().toString().trim();
        String contactNo = etContactno.getText().toString().trim();
        String dateOfBirth = etdateOfBirth.getText().toString().trim();
        String workLocation = workingPlaceName;
        String regNo = etRegNo.getText().toString().trim();
        if (fullName.isEmpty()) {
            etFullName.setError("Full name can't be empty!");
            return;
        }
        if (medicalCollege.isEmpty()) {
            etMedicalCollege.setError("Medical College name can't be empty");
            return;
        }
        if (contactNo.isEmpty()) {
            etContactno.setError("Contact no can't be empty");
            return;
        }
        if (dateOfBirth.isEmpty()) {
            etdateOfBirth.setError("Date of birth can't be empty");
            return;
        }
        if (workLocation.isEmpty()) {
            showToast("Please select your working location");
            return;
        }
        if (regNo.isEmpty()) {
            etRegNo.setError("Reg No can't be empty");
            return;
        }

        RequestParams params = new RequestParams();
        params.put("name", fullName);
        params.put("medicalcollege", medicalCollege);
        params.put("regno", regNo);
        params.put("contact", contactNo);
        params.put("designation", "doctor");
        params.put("dateofbirth", dateOfBirth);
        params.put("worklocation", workLocation);
        params.put("workinglat", workingPlaceLat);
        params.put("workinglon", workingPlaceLon);
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
                    showToast("Number already exists. Please login");
                    finish();

                } else {
                    DBHelper.setUserId(RegistrationActivity.this, response);
                    showToast("Account created successfully");
                    startActivity(new Intent(RegistrationActivity.this, NewsfeedActivity.class));
                    finish();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                //Log.d("--------", new String(responseBody));
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

                        Logger.d(phn);
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
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);

    }

    private void registerPlaceFragment() {

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.etWorkLocation);

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
