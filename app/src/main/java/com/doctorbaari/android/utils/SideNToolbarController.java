package com.doctorbaari.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.acvities.AllWorkLocationsActivity;
import com.doctorbaari.android.acvities.DoctorRegistrationActivity;
import com.doctorbaari.android.acvities.HomeActitvity;
import com.doctorbaari.android.acvities.HospitalAuthorityRegistration;
import com.doctorbaari.android.acvities.HowtoActivity;
import com.doctorbaari.android.acvities.InquiryActivity;
import com.doctorbaari.android.acvities.InternRegistration;
import com.doctorbaari.android.acvities.LoginActivity;
import com.doctorbaari.android.acvities.NewMedicineUpdatesActivity;
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


    public static void closeDrawer() {
        d.closeDrawer();
    }

    public static boolean isDrawerOpen() {
        return d.isDrawerOpen();
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
        //activity.overridePendingTransition(R.anim.enter, R.anim.exit);


    }

    private static void createDrawer(final Activity activity, Toolbar t)

    {
        PrimaryDrawerItem homeItem = new PrimaryDrawerItem().withName("Home").withIcon(R.drawable.home);
        PrimaryDrawerItem profileItem = new PrimaryDrawerItem().withName("Profile").withIcon(R.drawable.profile);
        PrimaryDrawerItem logout = new PrimaryDrawerItem().withName("Logout").withIcon(R.drawable.logout);
        PrimaryDrawerItem about = new PrimaryDrawerItem().withName("About").withIcon(R.drawable.about);
        PrimaryDrawerItem newMedicineUpdates = new PrimaryDrawerItem().withName("New Medicine Updates").withIcon(R.drawable.new_medicine_updates);
        PrimaryDrawerItem sendInvitaion = new PrimaryDrawerItem().withName("Invite Other Doctors").withIcon(R.drawable.invite);
        PrimaryDrawerItem addNewWorkPlace = new PrimaryDrawerItem().withName("Add New Workplace").withIcon(R.drawable.addworkplace);
        PrimaryDrawerItem howToUseDoctorBaari = new PrimaryDrawerItem().withName("How to use DoctorBaari").withIcon(R.drawable.howto);
        PrimaryDrawerItem checkForUpdates = new PrimaryDrawerItem().withName("Check for update").withIcon(R.drawable.update);

        d = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(t)

                .withGenerateMiniDrawer(true)

                .addDrawerItems(
                        homeItem, //0
                        new DividerDrawerItem(),
                        profileItem, //2
                        new DividerDrawerItem(),
                        addNewWorkPlace, //4
                        new DividerDrawerItem(),
                        newMedicineUpdates, //6
                        new DividerDrawerItem(),
                        howToUseDoctorBaari, //8
                        new DividerDrawerItem(),
                        sendInvitaion, //10
                        new DividerDrawerItem(),
                        about, //12
                        new DividerDrawerItem(),
                        checkForUpdates, //14
                        new DividerDrawerItem(),
                        logout //16

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
                                activity.startActivity(new Intent(activity, AllWorkLocationsActivity.class));
                                break;

                            case 8:
                                closeDrawer();
                                activity.startActivity(new Intent(activity, HowtoActivity.class));
                                break;

                            case 6:
                                closeDrawer();
                                activity.startActivity(new Intent(activity, NewMedicineUpdatesActivity.class));

                                break;

                            case 10:
                                closeDrawer();
                                String invitationMessage = "Hey there! DoctorBaari is a smart platform dedicated to MBBS " +
                                        "and intern doctors to help them finding Permanent or Temporary jobs. " +
                                        "You can also find Substitute doctors for your duty on this on this place. " +
                                        "Why don't you give it a try? Click the link below to download DoctorBaari app from PlayStore.";
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, invitationMessage);
                                sendIntent.setType("text/plain");
                                activity.startActivity(sendIntent);
                            case 12:
                                closeDrawer();
                                HelperFunc.openUrlInBrowser(activity, "https://doctorbaari.com/#menu#about");
                                break;

                            case 14:
                                closeDrawer();
                                final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    HelperFunc.openUrlInBrowser(activity, "https://play.google.com/store/apps/details?id=" + appPackageName);
                                }
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


                        }
                        return true;
                    }
                })
                .withSelectedItem(-1)
                .build();
        d.closeDrawer();

    }
}
