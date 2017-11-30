package com.doctorbaari.android.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.models.Job;

/**
 * Created by Nowfel Mashnoor on 11/7/2017.
 */

public class JobAdapter extends BaseAdapter {
    private Activity activity;
    private Job[] posts;

    public JobAdapter(Activity activity, Job[] posts) {
        this.activity = activity;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.length;
    }

    @Override
    public Job getItem(int i) {
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
            v = layoutInflater.inflate(R.layout.row_job, null);
        }
        TextView tvInstitueName = v.findViewById(R.id.tvInstituteName);
        TextView tvLocation = v.findViewById(R.id.tvLocation);
        TextView postedOn = v.findViewById(R.id.tvPostedOn);

        ImageView iv = v.findViewById(R.id.ivLocation);
        Job currpost = getItem(i);
        tvInstitueName.setText(currpost.getInstitute());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:0,0?q=" + ("40.7890011, -124.1719112")));
                try {
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        postedOn.setText("Posted On: " + currpost.getPostDatetime());
        tvLocation.setText("Location: " + currpost.getPlace());
        return v;
    }
}
