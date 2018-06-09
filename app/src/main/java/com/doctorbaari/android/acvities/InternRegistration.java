package com.doctorbaari.android.acvities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class InternRegistration extends AppCompatActivity {

    @BindView(R.id.etFullName)
    EditText etFullName;

    @BindView(R.id.etContactNo)
    EditText etContactno;

    @BindView(R.id.etMedicalCollegeName)
    EditText etMedicalCollege;

    @BindView(R.id.etRegistrationNo)
    EditText etRegNo;

    ProgressDialog dialog;
    AsyncHttpClient client;
    final static int APP_REQUEST_CODE = 55;
    String phoneNumber = "";
    String workingPlaceName = "";
    String workingPlaceLat = "";
    String workingPlaceLon = "";


    String email = "";
    String profileLink = "";
    String imageLink = "";

    File imgfile = null;
    @BindView(R.id.ivCertificateImage)
    ImageView ivCertificateImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern_registration);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Intern Doctor Registration");
        registerPlaceFragment();

        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting with Doctor Baari server...");

        verifyNumber();


    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public void goSignup(View v) {

        String fullName = etFullName.getText().toString().trim();
        String medicalCollege = etMedicalCollege.getText().toString();
        String contactNo = etContactno.getText().toString().trim();
        String dateOfBirth = "Not Available";
        String workLocation = workingPlaceName;
        String regNo = etRegNo.getText().toString().trim();

        String degree = "Intern";
        if (degree.isEmpty() || fullName.isEmpty() ||
                medicalCollege.isEmpty() || contactNo.isEmpty() || dateOfBirth.isEmpty() || workLocation.isEmpty() ||
                regNo.isEmpty() || workingPlaceName.isEmpty() || imgfile == null) {
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
        params.put("type", "intern");
        params.put("email", email);
        params.put("link", profileLink);
        params.put("picture_url", imageLink);
        params.put("token", FirebaseInstanceId.getInstance().getToken());
        try {
            params.put("imagefile", imgfile);
        } catch (Exception e) {
            HelperFunc.showToast(this, "Temporary registration picture not selected");
            return;
        }

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
                    startActivity(new Intent(InternRegistration.this, LoginActivity.class));
                    finish();

                } else {
                    DBHelper.setUserId(InternRegistration.this, response);
                    DBHelper.setSignedInStatus(InternRegistration.this, true);
                    showToast("Account created successfully");
                    startActivity(new Intent(InternRegistration.this, ConnectWithFacebookActivity.class));
                    finish();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                //Log.d("--------", new String(responseBody));
                dialog.dismiss();

                showToast("Something went wrong");
            }
        });
    }


    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                HelperFunc.showToast(InternRegistration.this, "Something went wrong");
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                imgfile = imageFile;
                Glide.with(InternRegistration.this).load(imageFile.getAbsolutePath()).into(ivCertificateImage);

            }


        });


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
                        AccountKitActivity.ResponseType.TOKEN).setDefaultCountryCode("+880").setReadPhoneStateEnabled(false); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);

    }

    public void selectImage(View v) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {

                    EasyImage.openChooserWithGallery(InternRegistration.this, "Select Image", 0);
                }


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
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

    public void goTermsAndConditions(View v) {
        HelperFunc.openUrlInBrowser(this, "https://doctorbaari.com/#menu#terms");

    }
}
