package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PostPermanentJobActivity extends AppCompatActivity {

    @BindView(R.id.etInstitution)
    EditText etInstitution;
    @BindView(R.id.etStartingFrom)
    EditText etStatingFrom;
    @BindView(R.id.etPlaceName)
    EditText etplaceName;
    @BindView(R.id.spnrDegree)
    Spinner spnrDegree;
    @BindView(R.id.etDetails)
    EditText etDetails;

    AsyncHttpClient client;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_permanent_job);
        ButterKnife.bind(this);
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Getting data from server...");
    }

    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    public void goPost(View v)
    {
        String institution = etInstitution.getText().toString();
        String startingfrom = etStatingFrom.getText().toString();
        String placename = etplaceName.getText().toString();
        String degree = spnrDegree.getSelectedItem().toString();
        String details = etDetails.getText().toString();

        RequestParams params = new RequestParams();
        params.put("hospital", institution);
        params.put("division", placename);
        params.put("details", details);
        params.put("degree", degree);
        params.put("deadline", startingfrom);


        client.post(Constants.POST_PERMANENT_JOB, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                dialog.dismiss();

                showToast("Successfully posted permanent job");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
            {
                dialog.dismiss();
                showToast("Something went wrong. Try again later");

            }
        });

    }
}
