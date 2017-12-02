package com.doctorbaari.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.doctorbaari.android.R;
import com.doctorbaari.android.models.Avaibility;

/**
 * Created by Nowfel Mashnoor on 12/2/2017.
 */

public class AvaibilityListAdapter extends BaseAdapter {

    private Activity activity;
    private Avaibility[] avaibilities;

    public AvaibilityListAdapter(Activity activity, Avaibility[] avaibilities) {
        this.activity = activity;
        this.avaibilities = avaibilities;
    }

    @Override
    public int getCount() {
        return avaibilities.length;
    }

    @Override
    public Avaibility getItem(int i) {
        return avaibilities[i];
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
            v = layoutInflater.inflate(R.layout.row_avaibility, null);
        }

        TextView tvFromdate = v.findViewById(R.id.tvFromdate);
        TextView tvTodate = v.findViewById(R.id.tvTodate);
        TextView tvLocation = v.findViewById(R.id.tvLocation);
        Switch swtchAvailable = v.findViewById(R.id.swtchAvailable);

        Avaibility currAvaibility = getItem(i);
        tvFromdate.setText(currAvaibility.getFromDate());
        tvTodate.setText(currAvaibility.getToDate());
        tvLocation.setText(currAvaibility.getPlace());
        if (currAvaibility.getAvailable().equals("1")) {
            swtchAvailable.setChecked(true);
        } else {
            swtchAvailable.setChecked(false);
        }

        return v;
    }
}
