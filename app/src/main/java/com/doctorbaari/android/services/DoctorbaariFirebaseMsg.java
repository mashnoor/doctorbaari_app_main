package com.doctorbaari.android.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.doctorbaari.android.R;
import com.doctorbaari.android.acvities.SplashActivity;
import com.doctorbaari.android.models.Advertise;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.orhanobut.logger.Logger;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class DoctorbaariFirebaseMsg extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.


        String title = remoteMessage.getData().get("title");


        if (title.toLowerCase().equals("refresh")) {
            getAdvertises();
        } else {
            String body = remoteMessage.getData().get("body");
            Log.d(TAG, remoteMessage.getData().get("title"));
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            Log.d(TAG, "Token: " + FirebaseInstanceId.getInstance().getToken());

            showNotification(title, body, 5);

        }


    }

    private void showNotification(String title, String message, int id) {


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.main_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }


    private void getAdvertises() {
        SyncHttpClient client = new SyncHttpClient();
        client.get(Constants.GET_ADVERTISES, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Logger.d(response);
                Advertise[] advertises = Geson.g().fromJson(response, Advertise[].class);
                cancelAllPreviousAlarms();
                DBHelper.saveAdvertises(getApplicationContext(), advertises);
                for (Advertise advertise : advertises) {
                    setNotificationAlarm(advertise);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void cancelAllPreviousAlarms() {
        Advertise[] advertises = DBHelper.getAdvertises(getApplicationContext());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        for (Advertise advertise : advertises) {
            Intent alarm = new Intent(this, AlarmReceiver.class);
            alarm.putExtra("advertise", Geson.g().toJson(advertise));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, advertise.getId(), alarm, PendingIntent.FLAG_CANCEL_CURRENT);

            alarmManager.cancel(pendingIntent);
        }
    }

    private Calendar getCalenderInstaseForTime(int hour, int min) {
        Calendar timeOff9 = Calendar.getInstance();
        Logger.d(hour);
        Logger.d(min);

        timeOff9.set(Calendar.HOUR_OF_DAY, hour);
        timeOff9.set(Calendar.MINUTE, min);
        timeOff9.set(Calendar.SECOND, 0);
        if (timeOff9.before(Calendar.getInstance())) {
            timeOff9.add(Calendar.DATE, 1);
        }
        Logger.d(timeOff9.getTimeInMillis());
        return timeOff9;
    }


    private void setNotificationAlarm(Advertise advertise) {
        Intent alarm = new Intent(this, AlarmReceiver.class);
        alarm.putExtra("advertise", Geson.g().toJson(advertise));
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, advertise.getId(), alarm, PendingIntent.FLAG_CANCEL_CURRENT);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getCalenderInstaseForTime(advertise.getHour(), advertise.getMinute()).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Logger.d("Alarm Set for - ");
        Logger.d(advertise);

    }

}
