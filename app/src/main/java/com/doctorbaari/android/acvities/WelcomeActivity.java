package com.doctorbaari.android.acvities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Sidebar;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        android.support.v7.widget.Toolbar t = (android.support.v7.widget.Toolbar) getSupportActionBar().getCustomView();
        Sidebar.build(this, t);

    }
    private void showToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void goMbbs(View v) {
        CharSequence colors[] = new CharSequence[] {"Sign Up Using Facebook", "Alternative Sign Up"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case 0:
                        showToast("Coming Soon");
                        break;
                    case 1:
                        startActivity(new Intent(WelcomeActivity.this, RegistrationActivity.class));

                }

            }
        });
        builder.show();


    }
}
