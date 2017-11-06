package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.etFullName)
    EditText etFullName;
    @BindView(R.id.etMedicalCollege) EditText etMedicalCollege;
    @BindView(R.id.etContactNo) EditText etContactno;
    @BindView(R.id.etDateOfBirth)  EditText etdateOfBirth;
    @BindView(R.id.etWorkLocation) EditText etWorkLocation;
    @BindView(R.id.etRegistrationNo) EditText etRegNo;

    ProgressDialog dialog;
    AsyncHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting with Doctor Baari server...");
    }
    private void showToast(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    public void goSignup(View v)
    {
        String fullName = etFullName.getText().toString().trim();
        String medicalCollege = etMedicalCollege.getText().toString().trim();
        String contactNo = etContactno.getText().toString().trim();
        String dateOfBirth = etdateOfBirth.getText().toString().trim();
        String workLocation = etWorkLocation.getText().toString().trim();
        String regNo = etRegNo.getText().toString().trim();

        RequestParams params = new RequestParams();
        params.put("name", fullName);
        params.put("medicalcollege", medicalCollege);
        params.put("regno", regNo);
        params.put("contact", contactNo);
        params.put("designation", "doctor");
        params.put("dateofbirth", dateOfBirth);
        params.put("worklocation", workLocation);
        client.post(Constants.SIGNUP_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                showToast("Successfully created");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                Log.d("--------", new String(responseBody));
                showToast("Some error occured");
            }
        });
    }
}
