package com.doctorbaari.android.acvities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.doctorbaari.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class AdvertiseDetailActivity extends Activity {


    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvDetails)
    TextView tvDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advertise_detail);

        getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        setFinishOnTouchOutside(false);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra("title");
        String details = getIntent().getStringExtra("details");

        tvTitle.setText(title);
        tvDetails.setText(details);

    }

    public void finishThis(View v) {
        finish();
    }
}
