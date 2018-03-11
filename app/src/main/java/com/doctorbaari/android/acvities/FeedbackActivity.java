package com.doctorbaari.android.acvities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.SideNToolbarController;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        SideNToolbarController.attach(this, "Feedback");
    }

    public void goBack(View v)
    {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
