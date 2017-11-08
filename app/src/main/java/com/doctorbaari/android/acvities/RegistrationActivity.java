package com.doctorbaari.android.acvities;

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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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
    @BindView(R.id.etWorkLocation)
    EditText etWorkLocation;
    @BindView(R.id.etRegistrationNo)
    EditText etRegNo;

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
                new DatePickerDialog(RegistrationActivity.this, date, myCalendar
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
        String medicalCollege = etMedicalCollege.getText().toString().trim();
        String contactNo = etContactno.getText().toString().trim();
        String dateOfBirth = etdateOfBirth.getText().toString().trim();
        String workLocation = etWorkLocation.getText().toString().trim();
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
            etWorkLocation.setError("Work location can't be empty");
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
        client.post(Constants.SIGNUP_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                showToast("Account created successfully");

                startActivity(new Intent(RegistrationActivity.this, NewsfeedActivity.class));
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                //Log.d("--------", new String(responseBody));
                showToast("Some error occured");
            }
        });
    }
}
