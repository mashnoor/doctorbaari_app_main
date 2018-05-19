package com.doctorbaari.android.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.doctorbaari.android.R;

public class ListViewEmptyMessageSetter {

    public static void set(Activity target, ListView listView, String message) {
        View emptyView = target.getLayoutInflater().inflate(R.layout.no_result_layout, null);
        TextView tvEmptymsg = emptyView.findViewById(R.id.tvEmptymsg);
        tvEmptymsg.setText(message);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
    }
}
