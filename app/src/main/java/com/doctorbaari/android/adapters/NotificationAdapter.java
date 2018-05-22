package com.doctorbaari.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.models.Notification;

public class NotificationAdapter extends BaseAdapter {

    Activity activity;
    Notification[] notifications;

    public NotificationAdapter(Activity activity, Notification[] notifications)
    {
        this.activity = activity;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.length;
    }

    @Override
    public Notification getItem(int position) {
        return notifications[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(activity);
            v = layoutInflater.inflate(R.layout.row_notification, null);
        }

        TextView tvTitle = v.findViewById(R.id.tvNotificationTitle);
        TextView tvBody = v.findViewById(R.id.tvNotificationBody);
        TextView tvDate = v.findViewById(R.id.tvNotificationTime);

        Notification currNotification = getItem(position);

        tvTitle.setText(currNotification.getTitle());
        tvBody.setText(currNotification.getBody());
        tvDate.setText(currNotification.getPostDate());

        return v;
    }
}
