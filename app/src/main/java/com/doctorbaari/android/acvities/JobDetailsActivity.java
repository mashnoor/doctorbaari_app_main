package com.doctorbaari.android.acvities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doctorbaari.android.R;
import com.doctorbaari.android.models.Job;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.HelperFunc;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

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

    @BindView(R.id.btnViewJobCircularImage)
    Button btnViewJobCircularImage;


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
            tvFromDate.setText(job.getDeadline());
            tvTodate.setText("Not Available");
            tvJobType.setText("Permanent");


        } else {
            tvFromDate.setText(job.getDateFrom());
            tvTodate.setText(job.getDateTo());
            tvJobType.setText("Temporary");
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.no_image);
            requestOptions.error(R.drawable.no_image);


        }
        tvPostTime.setText(job.getPostDatetime());


        tvInstitute.setText(job.getInstitute());
        tvPreferredDegree.setText(job.getDegree());
        tvPlace.setText(job.getPlace());
        tvDetails.setText(job.getDetails());


    }
    public void viewJobCircular(View v)
    {
        if(job.getImageLink()==null || job.getImageLink().isEmpty() || job.getImageLink().equals("Not Available"))
        {
            HelperFunc.showToast(this, "Job Circular not available");
        }
        else
        {
            Intent i = new Intent(this, JobCircularFullscreenView.class);
            i.putExtra("imagelink", job.getImageLink());
            startActivity(i);
        }
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

    public void goContact(View v)
    {
        String[] options = new String[]{"Call", "View Facebook Profile"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Dexter.withActivity(JobDetailsActivity.this).withPermission(Manifest.permission.CALL_PHONE).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + job.getUser().getPhone()));
                            startActivity(intent);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            HelperFunc.showToast(JobDetailsActivity.this, "Call permission not granted!");

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }).check();
                } else {
                    if (!job.getUser().getFb_profile().contains("facebook.com")) {
                        HelperFunc.showToast(JobDetailsActivity.this, "Facebook profile not available");
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(job.getUser().getFb_profile()));
                        startActivity(browserIntent);
                    }

                }
            }
        });
        builder.show();
    }
}
