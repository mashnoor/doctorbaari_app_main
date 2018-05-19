package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.SideNToolbarController;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        SideNToolbarController.attach(this, "Notications");
    }
}
