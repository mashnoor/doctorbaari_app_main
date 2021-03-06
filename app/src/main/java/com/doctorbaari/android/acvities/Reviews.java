package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.ReviewsListAdapter;
import com.doctorbaari.android.models.Review;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.HelperFunc;
import com.doctorbaari.android.utils.ListViewEmptyMessageSetter;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class Reviews extends AppCompatActivity {

    AsyncHttpClient client;
    ProgressDialog dialog;

    @BindView(R.id.lvReviews)
    ListView lvReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Reviews");
        ListViewEmptyMessageSetter.set(this, lvReviews, "No Reviews");
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");
        loadReviews();
    }

    private void loadReviews() {
        //Set Empty Review

        Intent i = getIntent();
        String userid = i.getStringExtra("userid");
        RequestParams params = new RequestParams();
        params.put("reviewed_to", userid);
        client.post(Constants.GET_REVIEWS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                Review[] reviews = Geson.g().fromJson(response, Review[].class);
                ReviewsListAdapter adapter = new ReviewsListAdapter(Reviews.this, reviews);
                lvReviews.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                HelperFunc.showToast(Reviews.this, "Something went wrong");

                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        SideNToolbarController.closeDrawer();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


}
