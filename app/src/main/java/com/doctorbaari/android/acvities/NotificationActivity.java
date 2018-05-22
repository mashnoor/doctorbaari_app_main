package com.doctorbaari.android.acvities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.adapters.NotificationAdapter;
import com.doctorbaari.android.models.Notification;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.HelperFunc;
import com.doctorbaari.android.utils.ListViewEmptyMessageSetter;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class NotificationActivity extends AppCompatActivity {


    @BindView(R.id.lvNotifications)
    ListView lvNotifications;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Notifications");
        ListViewEmptyMessageSetter.set(this, lvNotifications, "No notificaions available");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Getting notifications. Please wait...");
        getNotifications();

    }

    private void getNotifications() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.GET_NOTIFICATIONS, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                dialog.dismiss();
                String response = new String(responseBody);
                Notification[] notifications = Geson.g().fromJson(response, Notification[].class);
                NotificationAdapter notificationAdapter = new NotificationAdapter(NotificationActivity.this, notifications);
                lvNotifications.setAdapter(notificationAdapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                dialog.dismiss();
                HelperFunc.showToast(NotificationActivity.this, "Something went wrong.");

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
