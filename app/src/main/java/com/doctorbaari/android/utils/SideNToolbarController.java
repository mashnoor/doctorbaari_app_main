package com.doctorbaari.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.acvities.DoctorRegistrationActivity;
import com.doctorbaari.android.acvities.HomeActitvity;
import com.doctorbaari.android.acvities.HospitalAuthorityRegistration;
import com.doctorbaari.android.acvities.InquiryActivity;
import com.doctorbaari.android.acvities.InternRegistration;
import com.doctorbaari.android.acvities.LoginActivity;
import com.doctorbaari.android.acvities.NewsfeedActivity;
import com.doctorbaari.android.acvities.PostPermanentJobActivity;
import com.doctorbaari.android.acvities.PostSubstituteActivity;
import com.doctorbaari.android.acvities.ProfileActivity;
import com.doctorbaari.android.acvities.SearchPermanentJob;
import com.doctorbaari.android.acvities.SearchSubstituteJobs;
import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by Nowfel Mashnoor on 11/8/2017.
 */

public class SideNToolbarController {
    private SideNToolbarController() {
    }

    static Drawer d;



    public static void closeDrawer()
    {
        d.closeDrawer();
    }

    public static void attach(final Activity activity, String name)

    {


        Toolbar t = activity.findViewById(R.id.toolbar);


        TextView activityName = activity.findViewById(R.id.tvActivityName);
        ImageView ivRefresh = activity.findViewById(R.id.ivRefresh);
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.recreate();
            }
        });
        if ((activity instanceof DoctorRegistrationActivity) || (activity instanceof InternRegistration) || (activity instanceof HospitalAuthorityRegistration)) {
            ivRefresh.setVisibility(View.INVISIBLE);
        } else {
            createDrawer(activity, t);
        }


        activityName.setText(name);


    }

    private static void createDrawer(final Activity activity, Toolbar t)

    {
        PrimaryDrawerItem homeItem = new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.home);
        PrimaryDrawerItem profileItem = new PrimaryDrawerItem().withName("Profile").withIcon(R.drawable.profile);
        PrimaryDrawerItem postPermanent = new PrimaryDrawerItem().withName("Post Permanent Job").withIcon(R.drawable.postpermanentjob);
        PrimaryDrawerItem postSub = new PrimaryDrawerItem().withName("Post for Substitute").withIcon(R.drawable.postsubjob);
        PrimaryDrawerItem searchPermanent = new PrimaryDrawerItem().withName("Search for Permanent job").withIcon(R.drawable.searchpermanentjob);
        PrimaryDrawerItem searchSubstitute = new PrimaryDrawerItem().withName("Search Substitute Doctor").withIcon(R.drawable.searchsubjob);


        PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.logout);
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About").withIcon(R.drawable.about);
        PrimaryDrawerItem inquiry = new PrimaryDrawerItem().withName("Hospital Inquiry").withIcon(R.drawable.inquiry);
        PrimaryDrawerItem inviteOthers = new PrimaryDrawerItem().withName("Send Invitation").withIcon(R.drawable.invite);


        d = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(t)

                .withGenerateMiniDrawer(true)

                .addDrawerItems(
                        homeItem, //0
                        new DividerDrawerItem(),
                        profileItem, //2
                        new DividerDrawerItem(),
                        postPermanent, // 4
                        new DividerDrawerItem(),
                        postSub, //6
                        new DividerDrawerItem(),
                        searchPermanent, //8
                        new DividerDrawerItem(),
                        searchSubstitute, //10
                        new DividerDrawerItem(),
                        inquiry, //12
                        new DividerDrawerItem(),
                        about, //14
                        new DividerDrawerItem(),
                        logout, //16
                        new DividerDrawerItem(),
                        inviteOthers //18


                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("-------------", "" + position);
                        switch (position) {
                            case 0:
                                closeDrawer();
                                activity.startActivity(new Intent(activity, HomeActitvity.class));
                                break;
                            case 2:
                                closeDrawer();
                                activity.startActivity(new Intent(activity, ProfileActivity.class));
                                break;
                            case 4:
                                closeDrawer();
                                activity.startActivity(new Intent(activity, PostPermanentJobActivity.class));
                                break;
                            case 6:
                                closeDrawer();
                                activity.startActivity(new Intent(activity, PostSubstituteActivity.class));
                                break;
                            case 8:
                                closeDrawer();
                                activity.startActivity(new Intent(activity, SearchPermanentJob.class));

                                break;

                            case 10:
                                closeDrawer();
                                activity.startActivity(new Intent(activity, SearchSubstituteJobs.class));

                                break;
                            case 12:
                                closeDrawer();
                                activity.startActivity(new Intent(activity, InquiryActivity.class));

                                break;

                            case 16:
                                AccountKit.logOut();
                                closeDrawer();
                                DBHelper.setSignedInStatus(activity, false);
                                activity.finishAffinity();
                                activity.startActivity(new Intent(activity, LoginActivity.class));
                                FacebookSdk.sdkInitialize(activity);
                                LoginManager.getInstance().logOut();
                                break;
                            case 18:
                                String invitationMessage = "Hey there! DoctorBaari is an awesome " +
                                        "job exchange platform for doctors and interns! Click the link to download from play store!";
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, invitationMessage);
                                sendIntent.setType("text/plain");
                                closeDrawer();
                                activity.startActivity(sendIntent);

                                break;


                        }
                        return true;
                    }
                })
                .withSelectedItem(-1)
                .build();
        d.closeDrawer();

    }
}
