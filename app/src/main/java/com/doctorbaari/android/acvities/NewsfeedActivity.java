package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.JobAdapter;
import com.doctorbaari.android.models.Job;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class NewsfeedActivity extends AppCompatActivity {

    @BindView(R.id.lvnewsFeed)
    ListView lvnewsFeed;

    JobAdapter adapter;
    AsyncHttpClient client;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Newsfeed");
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to Doctor Baari Server...");
        getNewsFeed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        client.cancelAllRequests(true);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void getNewsFeed() {
        client.get(Constants.GET_NEWSFEED, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Job[] posts = Geson.g().fromJson(response, Job[].class);
                adapter = new JobAdapter(NewsfeedActivity.this, posts);
                lvnewsFeed.setAdapter(adapter);
                dialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showToast("Some error occured");
                Log.d("-------------", new String(responseBody));
                dialog.dismiss();

            }
        });

    }

    public void goMySchedule(View v) {
        Intent i = new Intent(this, AvaibilityListActivity.class);
        i.putExtra("type", "sub");
        startActivity(i);
    }
    public void goNotification(View v)
    {
        Intent i = new Intent(this, NotificationActivity.class);
        startActivity(i);
    }
}
