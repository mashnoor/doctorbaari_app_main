package com.doctorbaari.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.models.SubPost;

/**
 * Created by Nowfel Mashnoor on 11/7/2017.
 */

public class NewsfeedAdapter extends BaseAdapter {
    private Activity activity;
    private SubPost[] posts;

    public NewsfeedAdapter(Activity activity, SubPost[] posts) {
        this.activity = activity;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.length;
    }

    @Override
    public SubPost getItem(int i) {
        return posts[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(activity);
            v = layoutInflater.inflate(R.layout.row_newsfeed, null);
        }
        TextView hospitalName = v.findViewById(R.id.tvHospitalName);
        TextView duration = v.findViewById(R.id.tvDuraion);
        TextView postedOn = v.findViewById(R.id.tvPostedOn);
        TextView location = v.findViewById(R.id.tvLocation);
        SubPost currpost = getItem(i);
        hospitalName.setText(currpost.getHospitalName());
        duration.setText("Duration: " + currpost.getDateFrom() + " to " + currpost.getDateTo());
        postedOn.setText("Posted On: " + currpost.getPostDate());
        location.setText(currpost.getDivision());
        return v;
    }
}
