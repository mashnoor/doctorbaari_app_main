package com.doctorbaari.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.models.Advertise;

public class AdvertiseAdapter extends BaseAdapter {

    Activity activity;
    Advertise[] advertises;

    public AdvertiseAdapter(Activity activity, Advertise[] advertises) {
        this.activity = activity;
        this.advertises = advertises;

    }

    @Override
    public int getCount() {
        return advertises.length;
    }

    @Override
    public Advertise getItem(int position) {
        return advertises[position];
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
            v = layoutInflater.inflate(R.layout.custom_notification, null);
        }

        TextView tvMedicineName = v.findViewById(R.id.tvMedicineName);
        TextView tvMedicineDescription = v.findViewById(R.id.tvMedicineDescription);
        TextView tvCompany = v.findViewById(R.id.tvCompanyName);
        Advertise currAdvertise = getItem(position);

        tvMedicineName.setText(currAdvertise.getTitle());
        tvMedicineDescription.setText(currAdvertise.getBody());
        tvCompany.setText(currAdvertise.getCompany());

        return v;
    }
}
