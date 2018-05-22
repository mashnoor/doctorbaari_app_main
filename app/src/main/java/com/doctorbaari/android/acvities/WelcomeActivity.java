package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.SideNToolbarController;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }

    public void goDoctorSignUp(View v) {
        startActivity(new Intent(WelcomeActivity.this, DoctorRegistrationActivity.class));

    }

    public void goInternSignup(View v) {
        startActivity(new Intent(this, InternRegistration.class));
    }

    public void goHospitalAuthoritySignup(View v) {
        startActivity(new Intent(this, HospitalAuthorityRegistration.class));
    }


}
