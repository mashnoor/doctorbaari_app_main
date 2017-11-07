package com.doctorbaari.android.utils;

import android.app.Activity;
import android.view.View;
import android.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * Created by Nowfel Mashnoor on 11/7/2017.
 */

public class Sidebar {
    public Sidebar(){}
    public static void build(Activity activity, android.support.v7.widget.Toolbar t)
    {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Test");

//create the drawer and remember the `Drawer` result object
        new DrawerBuilder()
                .withActivity(activity)
                .withDisplayBelowStatusBar(true)


                .addDrawerItems(
                        item1


                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D

                        return true;
                    }
                })
                .build();
    }
}
