package com.doctorbaari.android.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.models.WorkLocation;

/**
 * Created by mashnoor on 12/3/18.
 */

public class WorkLoacationAdapter extends BaseAdapter {

    Activity activity;
    WorkLocation[] locations;

    public WorkLoacationAdapter(Activity activity, WorkLocation[] locations) {
        this.activity = activity;
        this.locations = locations;
    }

    @Override
    public int getCount() {
        return locations.length;
    }

    @Override
    public WorkLocation getItem(int position) {
        return locations[position];
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
            v = layoutInflater.inflate(R.layout.row_worklocation, null);
        }

        TextView tvWorklocation = v.findViewById(R.id.tvWorkLocation);
        Button btnLocation = v.findViewById(R.id.btnLocation);

        final WorkLocation currLocation = getItem(position);
        tvWorklocation.setText(currLocation.getPlace());


        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:0,0?q=" + (currLocation.getPlacelat() + ", " + currLocation.getPlacelon())));
                try {
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }
}
