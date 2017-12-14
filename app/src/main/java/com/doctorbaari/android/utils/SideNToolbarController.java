package com.doctorbaari.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.acvities.LoginActivity;
import com.doctorbaari.android.acvities.NewsfeedActivity;
import com.doctorbaari.android.acvities.PostPermanentJobActivity;
import com.doctorbaari.android.acvities.PostSubstituteActivity;
import com.doctorbaari.android.acvities.ProfileActivity;
import com.doctorbaari.android.acvities.SearchPermanentJob;
import com.doctorbaari.android.acvities.SearchSubstituteJobs;
import com.doctorbaari.android.acvities.ViewAvailableDoctorsActivity;
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
    public SideNToolbarController() {
    }

    public static void attach(final Activity activity, String name) {

        PrimaryDrawerItem feedItem = new PrimaryDrawerItem().withName("Feed").withIcon(R.drawable.newsfeed);
        PrimaryDrawerItem profileItem = new PrimaryDrawerItem().withName("Profile").withIcon(R.drawable.profileicon);
        PrimaryDrawerItem postPermanent = new PrimaryDrawerItem().withName("Post Permanent Job").withIcon(R.drawable.postjob);
        PrimaryDrawerItem postSub = new PrimaryDrawerItem().withName("Post for Substitute").withIcon(R.drawable.postjob);
        PrimaryDrawerItem searchPermanent = new PrimaryDrawerItem().withName("Search for Permanent job").withIcon(R.drawable.search);
        PrimaryDrawerItem searchSubstitute = new PrimaryDrawerItem().withName("Search for Substitute job").withIcon(R.drawable.search);


        PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.logout);
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About").withIcon(R.drawable.about);
        TextView activityName = activity.findViewById(R.id.tvActivityName);

        activityName.setText(name);

        Toolbar t = activity.findViewById(R.id.toolbar);


        final Drawer d = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(t)

                .withGenerateMiniDrawer(true)

                .addDrawerItems(
                        feedItem, //0
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
                        about, //12
                        new DividerDrawerItem(),
                        logout //14


                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("-------------", "" + position);
                        switch (position) {
                            case 0:
                                activity.startActivity(new Intent(activity, NewsfeedActivity.class));
                                break;
                            case 2:
                                activity.startActivity(new Intent(activity, ProfileActivity.class));
                                break;
                            case 4:
                                activity.startActivity(new Intent(activity, PostPermanentJobActivity.class));
                                break;
                            case 6:
                                activity.startActivity(new Intent(activity, PostSubstituteActivity.class));
                                break;
                            case 8:
                                activity.startActivity(new Intent(activity, SearchPermanentJob.class));
                                break;

                            case 10:
                                activity.startActivity(new Intent(activity, SearchSubstituteJobs.class));
                                break;

                            case 14:
                                AccountKit.logOut();
                                DBHelper.setSignedInStatus(activity, false);
                                activity.finishAffinity();
                                activity.startActivity(new Intent(activity, LoginActivity.class));
                                FacebookSdk.sdkInitialize(activity);
                                LoginManager.getInstance().logOut();
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
