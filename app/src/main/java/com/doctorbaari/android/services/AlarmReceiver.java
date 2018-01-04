package com.doctorbaari.android.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;


import com.doctorbaari.android.R;
import com.doctorbaari.android.acvities.AdvertiseDetailActivity;
import com.doctorbaari.android.acvities.ProfileDetail;
import com.doctorbaari.android.models.Advertise;
import com.doctorbaari.android.utils.Geson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


/**
 * Created by Nowfel Mashnoor on 12/16/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("Alarm Worked!!!");

        Advertise advertise = Geson.g().fromJson(intent.getStringExtra("advertise"), Advertise.class);
        Logger.d(advertise.toString());
        sendNotification(context, advertise.getTitle(), advertise.getBody(), advertise.getDetails(), advertise.getId());
    }

    public void sendNotification(Context context, String title, String description, String details, int id) {

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.main_logo)
                        .setContentTitle(title)
                        .setContentText(description);


        // Gets an instance of the NotificationManager service//
        Intent intent = new Intent(context, AdvertiseDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("details", details);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, 0);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.customnotificaiton));
        mBuilder.setAutoCancel(true);
        mBuilder.setColor(Color.GREEN);

        

        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManager mNotificationManager =

                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(id, mBuilder.build());
    }
}
