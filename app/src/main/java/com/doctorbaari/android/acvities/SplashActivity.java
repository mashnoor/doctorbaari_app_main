package com.doctorbaari.android.acvities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.doctorbaari.android.R;
import com.doctorbaari.android.models.Advertise;
import com.doctorbaari.android.services.AlarmReceiver;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


public class SplashActivity extends AppCompatActivity {


    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        client = new AsyncHttpClient();
        Logger.addLogAdapter(new AndroidLogAdapter());
        getAdvertises();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, HomeActitvity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2000);


    }

    private void getAdvertises() {
        client.get(Constants.GET_ADVERTISES, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Logger.d(response);
                Advertise[] advertises = Geson.g().fromJson(response, Advertise[].class);
                cancelAllPreviousAlarms();
                DBHelper.saveAdvertises(SplashActivity.this, advertises);
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
        Advertise[] advertises = DBHelper.getAdvertises(SplashActivity.this);
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


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getCalenderInstaseForTime(advertise.getHour(), advertise.getMinute()).getTimeInMillis(), 60000, pendingIntent);
        Logger.d("Alarm Set for - ");
        Logger.d(advertise);

    }

}
