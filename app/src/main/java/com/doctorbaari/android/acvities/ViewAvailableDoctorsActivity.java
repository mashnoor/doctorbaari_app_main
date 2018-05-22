package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.DoctorAdapter;
import com.doctorbaari.android.models.DoctorSub;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.HelperFunc;
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

public class ViewAvailableDoctorsActivity extends AppCompatActivity {


    @BindView(R.id.lvSubstitute)
    ListView lvSubstitute;

    AsyncHttpClient client;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_available_doctors);
        ButterKnife.bind(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        SideNToolbarController.attach(this, "Available Doctors' List");

        ListViewEmptyMessageSetter.set(this, lvSubstitute, "No doctor available right now. Check the updates later.");
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting with server...");
        searchAvailableDoctors();

    }


    public void searchAvailableDoctors() {

        Intent i = getIntent();
        RequestParams params = new RequestParams();
        String fromDate = i.getStringExtra("fromdate");
        String toDate = i.getStringExtra("todate");
        String preferredDegree = i.getStringExtra("degrees");

        String placelat = i.getStringExtra("placelat");
        String placelon = i.getStringExtra("placelon");
        String type = i.getStringExtra("type");

        Logger.d("From Date = " + fromDate + " To Date: " + toDate + " Degrees: " + preferredDegree + " lat: " +
                placelat + " lon: " + placelon + " type: " + type);

        params.put("fromdate", fromDate);
        params.put("todate", toDate);

        params.put("placelat", placelat);
        params.put("placelon", placelon);
        params.put("degree", preferredDegree);
        params.put("type", type);
        params.put("userid", DBHelper.getUserid(ViewAvailableDoctorsActivity.this));
        client.post(Constants.SEARCH_AVAILABLE_DOCTORS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Logger.d(response);
                DoctorSub[] subs = Geson.g().fromJson(response, DoctorSub[].class);
                DoctorAdapter adapter = new DoctorAdapter(ViewAvailableDoctorsActivity.this, subs);
                lvSubstitute.setAdapter(adapter);
                dialog.dismiss();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Logger.d(new String(responseBody));
                dialog.dismiss();
                HelperFunc.showToast(ViewAvailableDoctorsActivity.this, "Something went wrong");

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
