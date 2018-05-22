package com.doctorbaari.android.acvities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.HelperFunc;
import com.doctorbaari.android.utils.SideNToolbarController;

public class HowtoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howto);
        SideNToolbarController.attach(this, "How To");
    }

    public void goHowToSearchPermanent(View v)
    {
        HelperFunc.openUrlInBrowser(this, "https://doctorbaari.com/#menu#tutorial#permanent");
    }

    public void goHowToSearchTemporaryJob(View v)
    {
        HelperFunc.openUrlInBrowser(this, "https://doctorbaari.com/#menu#tutorial#temporary");
    }

    public void goHowToSearchSubstituteDoctor(View v)
    {
        HelperFunc.openUrlInBrowser(this, "https://doctorbaari.com/#menu#tutorial#substitute");
    }

    public void goHowToUseHospitalInquiries(View v)
    {
        HelperFunc.openUrlInBrowser(this, "https://doctorbaari.com/#menu#tutorial#inquiries");
    }

    public void goHowToPostPermanentJob(View v)
    {
        HelperFunc.openUrlInBrowser(this, "https://doctorbaari.com/#menu#tutorial#post");
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





}
