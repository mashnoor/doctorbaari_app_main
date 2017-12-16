package com.doctorbaari.android.acvities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import cz.msebera.android.httpclient.Header;

public class DoctorRegistrationActivity extends AppCompatActivity {

    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.spnrCollege)
    Spinner spnrMedicalCollege;
    @BindView(R.id.etContactNo)
    EditText etContactno;
    @BindView(R.id.etDateOfBirth)
    EditText etdateOfBirth;
    @BindView(R.id.login_button)
    LoginButton loginButton;

    CallbackManager callbackManager;


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
        Logger.addLogAdapter(new AndroidLogAdapter());
        verifyNumber();
        getCollegeList();
        registerLogin();
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
                new DatePickerDialog(DoctorRegistrationActivity.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void goSignup(View v) {

        String fullName = etFullName.getText().toString().trim();
        String medicalCollege = spnrMedicalCollege.getSelectedItem().toString();
        String contactNo = etContactno.getText().toString().trim();
        String dateOfBirth = etdateOfBirth.getText().toString().trim();
        String workLocation = workingPlaceName;
        String regNo = etRegNo.getText().toString().trim();

        String degree;
        JSONArray array = new JSONArray(degrees);
        degree = array.toString();
        if (degrees.isEmpty() || fullName.isEmpty() ||
                medicalCollege.isEmpty() || contactNo.isEmpty() || dateOfBirth.isEmpty() || workLocation.isEmpty() ||
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
                    DBHelper.setUserId(DoctorRegistrationActivity.this, response);
                    showToast("Account created successfully");
                    startActivity(new Intent(DoctorRegistrationActivity.this, NewsfeedActivity.class));
                    finish();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                //Log.d("--------", new String(responseBody));
                dialog.dismiss();
                Logger.d(new String(responseBody));
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
            try {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {

            }
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

    private void registerLogin() {

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));


        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.d(loginResult.getRecentlyGrantedPermissions());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    Logger.d(object);
                                    email = object.optString("email", "Not Available");
                                    profileLink = object.optString("link", "Not Available");
                                    imageLink = object.getJSONObject("picture").getJSONObject("data").optString("url", "Not Available");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // 01/31/1980 format
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,link,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                showToast("Cancelled Login");

            }

            @Override
            public void onError(FacebookException error) {

                showToast("Something went wrong");
            }
        });


    }

    private void getCollegeList() {
        client.get(Constants.GET_COLLEGE_LIST, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                String[] colleges = Geson.g().fromJson(response, String[].class);
                String[] collegesFinal = new String[colleges.length + 1];
                collegesFinal[0] = "Select your college";
                for (int i = 1; i <= colleges.length; i++) {
                    collegesFinal[i] = colleges[i - 1];
                }
                spnrMedicalCollege.setPrompt("Select Medical College");

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                        (DoctorRegistrationActivity.this, android.R.layout.simple_spinner_item,
                                collegesFinal); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spnrMedicalCollege.setAdapter(spinnerArrayAdapter);
                dialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showToast("Something went wrong");
                dialog.dismiss();

            }
        });
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

    public void degreeChecked(View v) {
        CheckBox cb = (CheckBox) v;

        if (cb.isChecked()) {
            Logger.d(cb.getText().toString());
            degrees.add(cb.getText().toString());
        } else {
            degrees.remove(cb.getText().toString());
        }

    }
}
