package com.doctorbaari.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.doctorbaari.android.R;
import com.doctorbaari.android.acvities.LoginActivity;
import com.doctorbaari.android.acvities.PostPermanentJobActivity;
import com.doctorbaari.android.acvities.PostSubstituteActivity;
import com.doctorbaari.android.acvities.ProfileActivity;
import com.doctorbaari.android.acvities.SearchPermanentJob;
import com.facebook.accountkit.AccountKit;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by Nowfel Mashnoor on 11/8/2017.
 */

public class SideBar {
    public SideBar() {
    }

    public static void attach(final Activity activity) {

        PrimaryDrawerItem feedItem = new PrimaryDrawerItem().withName("Feed").withIcon(R.drawable.newsfeed);
        PrimaryDrawerItem profileItem = new PrimaryDrawerItem().withName("Profile").withIcon(R.drawable.profileicon);
        PrimaryDrawerItem searchJob = new PrimaryDrawerItem().withName("Post Permanent Job").withIcon(R.drawable.search);
        PrimaryDrawerItem searchSub = new PrimaryDrawerItem().withName("Post for Substitute").withIcon(R.drawable.search);
        PrimaryDrawerItem searchPermanent = new PrimaryDrawerItem().withName("Search for Permanent job").withIcon(R.drawable.search);


        PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.logout);
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About").withIcon(R.drawable.about);


        new DrawerBuilder()
                .withActivity(activity)
                .withGenerateMiniDrawer(true)

                .addDrawerItems(
                        feedItem,
                        new DividerDrawerItem(),
                        profileItem,
                        new DividerDrawerItem(),
                        searchJob,
                        new DividerDrawerItem(),
                        searchSub,
                        new DividerDrawerItem(),
                        logout,
                        new DividerDrawerItem(),
                        about,
                        new DividerDrawerItem(),
                        searchPermanent


                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.d("-------------", "" + position);
                        switch (position) {
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
                                AccountKit.logOut();
                                DBHelper.setSignedInStatus(activity, false);
                                activity.finish();
                                activity.startActivity(new Intent(activity, LoginActivity.class));
                                break;
                            case 10:
                                activity.startActivity(new Intent(activity, SearchPermanentJob.class));
                                break;

                        }
                        return true;
                    }
                })
                .withSelectedItem(-1)
                .build();
    }
}
