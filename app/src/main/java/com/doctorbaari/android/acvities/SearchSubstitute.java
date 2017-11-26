package com.doctorbaari.android.acvities;

import android.app.AlertDialog;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.SideBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchSubstitute extends AppCompatActivity {

    @BindView(R.id.etInstituteName)
    EditText etInstituteName;
    @BindView(R.id.spnrDivision)
    Spinner spnrDivision;
    @BindView(R.id.spnrThana)
    Spinner spnrThana;
    @BindView(R.id.spnrZilla)
    Spinner spnrZilla;
    @BindView(R.id.etDetails)
    EditText etDetails;

    @BindView(R.id.tvFromDate)
    TextView tvDateFrom;
    @BindView(R.id.tvToDate)
    TextView tvToDate;
    AsyncHttpClient client;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_substitute);
        ButterKnife.bind(this);
        SideBar.attach(this);

        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to Doctor Baari server...");
        final int[] which = {0};


        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                if (which[0] == 0)
                    tvDateFrom.setText(year + "-" + monthOfYear + "-" + dayOfMonth);

                else
                    tvToDate.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }

        };

        tvDateFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                which[0] = 0;
                new DatePickerDialog(SearchSubstitute.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                which[0] = 1;
                new DatePickerDialog(SearchSubstitute.this, AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


    }

    public void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void goPost(View v) {
        String instituteName = etInstituteName.getText().toString().trim();
        String division = spnrDivision.getSelectedItem().toString();
        String thana = spnrThana.getSelectedItem().toString();
        String zilla = spnrZilla.getSelectedItem().toString();
        String details = etDetails.getText().toString();

        String date_to = tvToDate.getText().toString();
        String date_from = tvDateFrom.getText().toString();
        RequestParams params = new RequestParams();
        params.put("date_to", date_to);
        params.put("date_from", date_from);
        params.put("division", division);
        params.put("division", division);
        params.put("thana", thana);
        params.put("zilla", zilla);
        params.put("hospital", instituteName);
        params.put("details", details);
        params.put("username", "ABC");
        client.post(Constants.POST_SUB_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                dialog.dismiss();
                showToast("Successfully Posted");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                dialog.dismiss();
                Log.d("--------", new String(responseBody));
            }
        });


    }

}
