package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.JobAdapter;
import com.doctorbaari.android.models.Job;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.lvHistory)
    ListView lvHistory;

    ProgressDialog dialog;
    AsyncHttpClient client;
    Job[] posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");
        client = new AsyncHttpClient();

        loadHistory();
        Logger.addLogAdapter(new AndroidLogAdapter());

    }
    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void loadHistory()
    {
        RequestParams params = new RequestParams();
        params.put("userid", DBHelper.getUserid(HistoryActivity.this));
        client.post(Constants.GET_HISTORY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                posts = Geson.g().fromJson(response, Job[].class);
                JobAdapter adapter = new JobAdapter(HistoryActivity.this, posts);

                lvHistory.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showToast("Something went wrong");
                dialog.dismiss();
                Logger.d(new String(responseBody));


            }
        });
    }
}
