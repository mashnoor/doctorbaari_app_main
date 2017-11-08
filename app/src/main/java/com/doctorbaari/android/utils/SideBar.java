package com.doctorbaari.android.utils;

import android.app.Activity;
import android.view.View;

import com.doctorbaari.android.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

/**
 * Created by Nowfel Mashnoor on 11/8/2017.
 */

public class SideBar {
    public SideBar(){}

    public static void attach(Activity activity)
    {
        PrimaryDrawerItem feedItem = new PrimaryDrawerItem().withName("Feed").withIcon(R.drawable.newsfeed);
        PrimaryDrawerItem searchJob = new PrimaryDrawerItem().withName("Search Job").withIcon(R.drawable.searchjob);
        PrimaryDrawerItem searchSub = new PrimaryDrawerItem().withName("Feed").withIcon(R.drawable.searchsub);
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.logout);
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About").withIcon(R.drawable.about);
        AccountHeader headerResult = new AccountHeaderBuilder()                .withActivity(activity)

                .withHeaderBackground(R.drawable.profilebackground)

                .addProfiles(
                        new ProfileDrawerItem().withName("Person Name").withEmail("0123456789").withIcon(activity.getResources().getDrawable(R.drawable.placeholder))
                )
                .build();


        new DrawerBuilder()
                .withActivity(activity)
                .withGenerateMiniDrawer(true)
                .withAccountHeader(headerResult)

                .addDrawerItems(
                        feedItem,
                        new DividerDrawerItem(),
                        searchJob,
                        new DividerDrawerItem(),
                        searchSub,
                        new DividerDrawerItem(),
                        logout,
                        new DividerDrawerItem(),
                        about


                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        return true;
                    }
                })
                .withSelectedItem(-1)
                .build();
    }
}
