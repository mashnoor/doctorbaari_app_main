package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.doctorbaari.android.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }
    public void goLogin(View v)
    {
        finish();
        startActivity(new Intent(this, NewsfeedActivity.class));
    }
    public void goRegistration(View v)
    {
        startActivity(new Intent(this, WelcomeActivity.class));
    }
}
