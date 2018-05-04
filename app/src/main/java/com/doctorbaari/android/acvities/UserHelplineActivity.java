package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doctorbaari.android.R;

public class UserHelplineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_helpline);
    }

    public void goHowTo(View v) {
        startActivity(new Intent(this, HowtoActivity.class));
    }

    public void goAvaibilityActivity(View v) {
        Intent i = new Intent(this, AvaibilityListActivity.class);
        i.putExtra("type", "all");
        startActivity(i);
    }
}
