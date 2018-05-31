package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.WorkLoacationAdapter;
import com.doctorbaari.android.models.WorkLocation;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.HelperFunc;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.fabric.sdk.android.Fabric;

public class AllWorkLocationsOtherUserActivity extends AppCompatActivity {

    ProgressDialog dialog;

    @BindView(R.id.lvWorkLocations)
    ListView lvWorkLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_work_locations_other_user);

        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "My Work Locations");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading. Please wait...");
        loadWorkLocations();
    }
    @Override
    protected void onPause() {
        super.onPause();
        SideNToolbarController.closeDrawer();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onBackPressed() {

        if(SideNToolbarController.isDrawerOpen())
            SideNToolbarController.closeDrawer();
        else
            super.onBackPressed();
    }


    private void loadWorkLocations() {
        String userid = getIntent().getStringExtra("userid");

        RequestParams params = new RequestParams("userid", userid);

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.GET_ALL_WORK_LOCATIONS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                WorkLocation[] locations = Geson.g().fromJson(response, WorkLocation[].class);
                WorkLoacationAdapter adapter = new WorkLoacationAdapter(AllWorkLocationsOtherUserActivity.this, locations);
                lvWorkLocations.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                HelperFunc.showToast(AllWorkLocationsOtherUserActivity.this, "Something went wrong");
                dialog.dismiss();

            }
        });
    }
}
