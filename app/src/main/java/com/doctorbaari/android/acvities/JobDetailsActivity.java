package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doctorbaari.android.R;
import com.doctorbaari.android.models.Job;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.SideNToolbarController;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobDetailsActivity extends AppCompatActivity {


    @BindView(R.id.tvPostTime)
    TextView tvPostTime;

    @BindView(R.id.tvFromDate)
    TextView tvFromDate;

    @BindView(R.id.tvToDate)
    TextView tvTodate;

    @BindView(R.id.tvInstitute)
    TextView tvInstitute;

    @BindView(R.id.tvPreferredDegree)
    TextView tvPreferredDegree;

    @BindView(R.id.tvPlace)
    TextView tvPlace;

    @BindView(R.id.tvJobType)
    TextView tvJobType;

    @BindView(R.id.tvDetails)
    TextView tvDetails;

    @BindView(R.id.ivPermanentJobImage)
    ImageView ivPermanentJobImage;


    Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Post Details");
        Intent i = getIntent();
        job = Geson.g().fromJson(i.getStringExtra("job"), Job.class);
        if (job.getType().equals("per")) {
            tvTodate.setVisibility(View.INVISIBLE);
            tvFromDate.setText("Deadline: " + job.getDeadline());
            tvJobType.setText("Job Type: Permanent");
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.no_image);
            requestOptions.error(R.drawable.no_image);

            Glide.with(JobDetailsActivity.this).load(job.getImageLink()).apply(requestOptions).into(ivPermanentJobImage);

        } else {
            tvFromDate.setText("From Date: " + job.getDateFrom());
            tvTodate.setText("To Date: " + job.getDateTo());
            tvJobType.setText("Job Type: Temporary");

        }
        tvPostTime.setText(job.getPostDatetime());


        tvInstitute.setText("Institute: " + job.getInstitute());
        tvPreferredDegree.setText("Preferred Degree: " + job.getDegree());
        tvPlace.setText("Place: " + job.getPlace());
        tvDetails.setText("Details: " + job.getDetails());


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void viewProfile(View v) {
        Intent i = new Intent(this, ProfileDetail.class);
        i.putExtra("userid", job.getUserid());
        startActivity(i);
    }
}
