package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doctorbaari.android.R;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class JobCircularFullscreenView extends AppCompatActivity {

    @BindView(R.id.ivJobCircular)
    PhotoView ivJobCircular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_job_circular_fullscreen_view);
        ButterKnife.bind(this);
        Intent i = getIntent();
        String imageLink = i.getStringExtra("imagelink");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.no_image);
        requestOptions.error(R.drawable.no_image);

        Glide.with(JobCircularFullscreenView.this).load(imageLink).apply(requestOptions).into(ivJobCircular);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
