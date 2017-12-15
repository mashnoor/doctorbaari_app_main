package com.doctorbaari.android.acvities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doctorbaari.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvertiseDetailActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvDetails)
    TextView tvDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise_detail);

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
