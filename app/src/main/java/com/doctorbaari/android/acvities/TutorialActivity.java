package com.doctorbaari.android.acvities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.SideNToolbarController;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        SideNToolbarController.attach(this, "Tutorial");
    }

    @Override
    protected void onPause() {
        super.onPause();
        SideNToolbarController.closeDrawer();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
}
