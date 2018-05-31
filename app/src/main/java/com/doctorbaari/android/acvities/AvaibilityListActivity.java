package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.AvailabilityListAdapter;
import com.doctorbaari.android.models.Availability;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.ListViewEmptyMessageSetter;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.fabric.sdk.android.Fabric;

public class AvaibilityListActivity extends AppCompatActivity {


    @BindView(R.id.lvAvaibilityList)
    ListView lvAvaibilityList;


    ProgressDialog dialog;
    AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaibility_list);

        ButterKnife.bind(this);

        SideNToolbarController.attach(this, "Job Search History");
        ListViewEmptyMessageSetter.set(this, lvAvaibilityList, "Availability Schedule Empty");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");
        client = new AsyncHttpClient();
        Logger.addLogAdapter(new AndroidLogAdapter());

        loadAvaibilityList();

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


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    private void loadAvaibilityList() {
        RequestParams params = new RequestParams();
        Logger.d(getIntent().getStringExtra("type"));
        params.put("type", getIntent().getStringExtra("type"));
        params.put("userid", DBHelper.getUserid(AvaibilityListActivity.this));
        client.post(Constants.GET_AVAIBILITY_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Availability[] avaibilities = Geson.g().fromJson(response, Availability[].class);
                AvailabilityListAdapter adapter = new AvailabilityListAdapter(AvaibilityListActivity.this, avaibilities);
                lvAvaibilityList.setAdapter(adapter);
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
