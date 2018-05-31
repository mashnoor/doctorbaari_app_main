package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.HelperFunc;
import com.doctorbaari.android.utils.SideNToolbarController;

public class UserHelplineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_helpline);
        SideNToolbarController.attach(this, "User Helpline");
    }

    public void goHowTo(View v) {
        startActivity(new Intent(this, HowtoActivity.class));
    }


    public void goFaq(View v)
    {
        HelperFunc.openUrlInBrowser(this, "https://doctorbaari.com/#menu#FAQ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SideNToolbarController.closeDrawer();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void goGroup(View v)
    {
        HelperFunc.openUrlInBrowser(this, "https://www.facebook.com/groups/623620854637015/");
    }

    public void goPage(View v)
    {
        HelperFunc.openUrlInBrowser(this, "https://www.facebook.com/DoctorBaari-Smart-job-exchanging-platform-for-Doctors-1689187547844056");
    }
}
