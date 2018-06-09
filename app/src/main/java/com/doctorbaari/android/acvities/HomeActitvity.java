package com.doctorbaari.android.acvities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.SideNToolbarController;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;


public class HomeActitvity extends AppCompatActivity {

    @BindView(R.id.slider)
    SliderLayout sliderShow;


    @Override
    protected void onStop() {
        super.onStop();
        sliderShow.stopAutoCycle();

    }

    @Override
    protected void onResume() {

        SideNToolbarController.closeDrawer();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SideNToolbarController.closeDrawer();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onBackPressed() {

        if (SideNToolbarController.isDrawerOpen())
            SideNToolbarController.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.enter, R.anim.exit);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Home");
        sliderShow = findViewById(R.id.slider);

        DefaultSliderView s1 = new DefaultSliderView(this);
        s1.image(R.drawable.s1);

        DefaultSliderView s2 = new DefaultSliderView(this);
        s2.image(R.drawable.s2);

        DefaultSliderView s3 = new DefaultSliderView(this);
        s3.image(R.drawable.s3);

        DefaultSliderView s4 = new DefaultSliderView(this);
        s4.image(R.drawable.s4);

        DefaultSliderView s5 = new DefaultSliderView(this);
        s5.image(R.drawable.s5);

        DefaultSliderView s6 = new DefaultSliderView(this);
        s6.image(R.drawable.s6);


        sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setDuration(8000);
        sliderShow.addSlider(s1);
        sliderShow.addSlider(s2);
        sliderShow.addSlider(s3);
        sliderShow.addSlider(s4);
        sliderShow.addSlider(s5);
        sliderShow.addSlider(s6);


        /***
         List<Banner> banners = new ArrayList<>();
         banners.add(new DrawableBanner(R.drawable.s1));
         banners.add(new DrawableBanner(R.drawable.s2));
         banners.add(new DrawableBanner(R.drawable.s3));
         banners.add(new DrawableBanner(R.drawable.s4));
         banners.add(new DrawableBanner(R.drawable.s5));
         banners.add(new DrawableBanner(R.drawable.s6));
         banners.add(new DrawableBanner(R.drawable.s7));
         banners.add(new DrawableBanner(R.drawable.s8));
         sliderLayout.setBanners(banners);
         ***/


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
        startActivity(new Intent(this, AvailabilityListActivity.class));
    }


    public void goFeedback(View v) {
        startActivity(new Intent(this, FeedbackActivity.class));
    }

    public void goInvite(View v) {
        String invitationMessage = "Hey there! DoctorBaari is a smart platform dedicated to MBBS " +
                "and intern doctors to help them finding Permanent or Temporary jobs. " +
                "You can also find Substitute doctors for your duty on this on this place. " +
                "Why don't you give it a try? Click the link below to download DoctorBaari app from PlayStore.";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, invitationMessage);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void goUserHelpline(View v) {
        startActivity(new Intent(this, UserHelplineActivity.class));
    }

    public void goViewHistory(View view) {
        Intent i = new Intent(this, HistoryActivity.class);
        i.putExtra("type", "all");
        startActivity(i);
    }


}
