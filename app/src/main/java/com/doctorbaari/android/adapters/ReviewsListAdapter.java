package com.doctorbaari.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.models.Review;

/**
 * Created by Nowfel Mashnoor on 12/7/2017.
 */

public class ReviewsListAdapter extends BaseAdapter {

    Review[] reviews;
    Activity activity;

    public ReviewsListAdapter(Activity activity, Review[] reviews)
    {
        this.reviews = reviews;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return reviews.length;
    }

    @Override
    public Review getItem(int i) {
        return reviews[i];
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
            v = layoutInflater.inflate(R.layout.row_review, null);
        }

        Review currReview = getItem(i);
        TextView tvReviewer = v.findViewById(R.id.tvReviewUserName);
        TextView tvReviewDetail = v.findViewById(R.id.tvReviewDetail);
        TextView tvRating = v.findViewById(R.id.tvRating);

        tvReviewer.setText(currReview.getReviewerName());
        tvReviewDetail.setText(currReview.getReview());
        tvRating.setText(currReview.getRating());


        return v;
    }
}
