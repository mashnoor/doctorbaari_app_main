package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toolbar;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.SideNToolbarController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.views.BannerSlider;

public class HomeActitvity extends AppCompatActivity {

    @BindView(R.id.slideImage)
    BannerSlider sliderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Home");



        List<Banner> banners=new ArrayList<>();
        banners.add(new DrawableBanner(R.drawable.s1));
        banners.add(new DrawableBanner(R.drawable.s2));
        sliderLayout.setBanners(banners);





    }


    public void goNewsFeed(View v) {
        startActivity(new Intent(this, NewsfeedActivity.class));
    }

    public void goPostedPermanentJobs(View v) {
        startActivity(new Intent(this, PostPermanentJobActivity.class));

    }

    public void goSearchPermanentJob(View v) {
        startActivity(new Intent(this, SearchPermanentJob.class));
    }

    public void goPostSubJobs(View v) {
        startActivity(new Intent(this, PostSubstituteActivity.class));
    }

    public void goSearchSubJobs(View v) {
        startActivity(new Intent(this, SearchSubstituteJobs.class));
    }

    public void goHospitalInquiry(View v) {
        startActivity(new Intent(this, InquiryActivity.class));
    }

    public void goAvailabilityActivity(View v) {
        startActivity(new Intent(this, AvaibilityListActivity.class));
    }

    public void goProfile(View v) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void goNewMedicineUpdates(View v) {
        //startActivity(new Intent(this, ));
    }

}
