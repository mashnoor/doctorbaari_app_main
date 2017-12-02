package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.DoctorAdapter;
import com.doctorbaari.android.models.DoctorSub;
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

public class HistorySubSearchResult extends AppCompatActivity {

    AsyncHttpClient client;

    ProgressDialog dialog;

    @BindView(R.id.lvSubstitute)
    ListView lvSubstitute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_sub_search_result);
        ButterKnife.bind(this);
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        Logger.d(new AndroidLogAdapter());

        dialog.setMessage("Connecting with server...");
        Intent i = getIntent();
        String fromDate = i.getStringExtra("fromdate");
        String todate = i.getStringExtra("todate");
        String placename = i.getStringExtra("place");
        String placelat = i.getStringExtra("placelat");
        String placelon = i.getStringExtra("placelon");
        String degree = i.getStringExtra("degree");
        loadSearchResult(fromDate, todate, placename, placelat, placelon, degree);

    }


    //This is for loadig the search result of the posted sub jobs

    public void loadSearchResult(String fromDate, String toDate, String placename, String placelat, String placelon, String preferredDegree) {
        RequestParams params = new RequestParams();

        params.put("fromdate", fromDate);
        params.put("todate", toDate);
        params.put("place", placename);
        params.put("placelat", placelat);
        params.put("placelon", placelon);
        params.put("degree", preferredDegree);
        params.put("userid", DBHelper.getUserid(HistorySubSearchResult.this));
        client.post(Constants.SEARCH_SUB, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                DoctorSub[] subs = Geson.g().fromJson(response, DoctorSub[].class);
                DoctorAdapter adapter = new DoctorAdapter(HistorySubSearchResult.this, subs);
                lvSubstitute.setAdapter(adapter);

                dialog.dismiss();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Logger.d(new String(responseBody));
                dialog.dismiss();

            }
        });


    }
}
