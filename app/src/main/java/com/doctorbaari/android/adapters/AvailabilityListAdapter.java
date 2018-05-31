package com.doctorbaari.android.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.acvities.SubstituteJobSearchResult;
import com.doctorbaari.android.models.Availability;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import cz.msebera.android.httpclient.Header;

/**
 * Created by Nowfel Mashnoor on 12/2/2017.
 */

public class AvailabilityListAdapter extends BaseAdapter {

    private Activity activity;
    private Availability[] avaibilities;

    public AvailabilityListAdapter(Activity activity, Availability[] avaibilities) {
        this.activity = activity;
        this.avaibilities = avaibilities;
    }

    @Override
    public int getCount() {
        return avaibilities.length;
    }

    @Override
    public Availability getItem(int i) {
        return avaibilities[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(activity);
            v = layoutInflater.inflate(R.layout.row_avaibility, null);
        }

        TextView tvFromdate = v.findViewById(R.id.tvFromdate);
        TextView tvTodate = v.findViewById(R.id.tvTodate);
        TextView tvLocation = v.findViewById(R.id.tvLocation);
        TextView tvJobType = v.findViewById(R.id.tvJobType);
        Button btnSeeResults = v.findViewById(R.id.btnSeeresults);
        final Switch swtchAvailable = v.findViewById(R.id.swtchAvailable);

        final Availability currAvaibility = getItem(i);

        btnSeeResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncHttpClient client = new AsyncHttpClient();
                final ProgressDialog dialog = new ProgressDialog(activity);
                dialog.setMessage("Connecting to server...");

                RequestParams params = new RequestParams();
                params.put("fromdate", currAvaibility.getFromDate());
                params.put("todate", currAvaibility.getToDate());
                params.put("userid", DBHelper.getUserid(activity));
                params.put("place", currAvaibility.getPlace());
                params.put("placelat", currAvaibility.getPlacelat());
                params.put("placelon", currAvaibility.getPlacelon());
                String whichUrlToHit;
                if (currAvaibility.getType().equals("per"))
                    whichUrlToHit = Constants.SEARCH_PERMANENT_JOB;
                else
                    whichUrlToHit = Constants.SEARCH_SUBSTITUTE_JOBS;

                client.post(whichUrlToHit, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        dialog.dismiss();
                        String response = new String(responseBody);
                        Intent i = new Intent(activity, SubstituteJobSearchResult.class);
                        i.putExtra("response", response);

                        i.putExtra("type", currAvaibility.getType());
                        activity.startActivity(i);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        dialog.dismiss();


                    }
                });


            }
        });

        tvFromdate.setText("From Date: " + currAvaibility.getFromDate());
        tvTodate.setText("To Date: " + currAvaibility.getToDate());
        tvLocation.setText("Location: " + currAvaibility.getPlace());
        if(currAvaibility.getType().equals("sub"))
            tvJobType.setText("Job Type: Temporary");
        else
            tvJobType.setText("Job Type: Permanent");
        if (currAvaibility.getAvailable().equals("1")) {
            swtchAvailable.setChecked(true);
        } else {
            swtchAvailable.setChecked(false);
        }

        swtchAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("availability_id", currAvaibility.getId());
                params.put("status", swtchAvailable.isChecked() ? 1 : 0);
                final ProgressDialog dialog = new ProgressDialog(activity);
                dialog.setMessage("Connecting to server...");
                client.post(Constants.CHANGE_STATUS, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        dialog.show();
                        getItem(i).setAvailable(swtchAvailable.isChecked() ? "1" : "0");

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        dialog.dismiss();
                        Toast.makeText(activity, "Successfully changed status", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
            }
        });


        return v;
    }


}
