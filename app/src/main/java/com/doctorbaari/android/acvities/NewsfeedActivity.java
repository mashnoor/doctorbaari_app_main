package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.NewsfeedAdapter;
import com.doctorbaari.android.models.SubPost;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.Geson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class NewsfeedActivity extends AppCompatActivity {

    @BindView(R.id.lvnewsFeed)
    ListView lvnewsFeed;

    NewsfeedAdapter adapter;
    AsyncHttpClient client;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        ButterKnife.bind(this);
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to Doctor Baari Server...");
        getNewsFeed();

    }

    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void getNewsFeed() {
        client.get(Constants.GET_ALL_SUBS_URL, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                SubPost[] posts = Geson.g().fromJson(response, SubPost[].class);
                adapter = new NewsfeedAdapter(NewsfeedActivity.this, posts);
                lvnewsFeed.setAdapter(adapter);
                dialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                showToast("Some error occured");
                dialog.dismiss();

            }
        });

    }
}
