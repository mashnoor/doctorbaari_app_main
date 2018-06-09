package com.doctorbaari.android.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.acvities.HistoryActivity;
import com.doctorbaari.android.acvities.JobDetailsActivity;
import com.doctorbaari.android.acvities.ViewAvailableDoctorsActivity;
import com.doctorbaari.android.models.Job;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.HelperFunc;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(activity);
            v = layoutInflater.inflate(R.layout.row_job, null);
        }

        final Job currpost = getItem(i);


        TextView tvInstitueName = v.findViewById(R.id.tvInstituteName);
        TextView tvLocation = v.findViewById(R.id.tvLocation);
        TextView postedOn = v.findViewById(R.id.tvPostedOn);
        TextView tvDistance = v.findViewById(R.id.tvDistance);
        TextView tvPostedBy = v.findViewById(R.id.tvPostedBy);

        Button ivLocation = v.findViewById(R.id.btnLocation);

        Button btnContact = v.findViewById(R.id.btnContact);
        Button seeDetails = v.findViewById(R.id.btnJobdetails);


        final Switch swtchJobAvailable = v.findViewById(R.id.swtchJobAvailable);


        seeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, JobDetailsActivity.class);
                String objectJson = Geson.g().toJson(currpost);
                i.putExtra("job", objectJson);
                activity.startActivity(i);
            }
        });

        tvDistance.setText(currpost.getDistance() + " KM");
        tvPostedBy.setText(currpost.getUsername());


        tvInstitueName.setText(currpost.getInstitute());
        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:0,0?q=" + (currpost.getPlacelat() + ", " + currpost.getPlacelon())));
                try {
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        postedOn.setText(currpost.getPostDatetime());
        tvLocation.setText(currpost.getPlace());

        //As the Newsfeed and schedule are from same adapter, replace the contact buttton with see result for schedule
        if (activity instanceof HistoryActivity) {
            btnContact.setText("View Update");
            tvDistance.setText("Not Available");

            if (currpost.getAvailable().equals("1"))
                swtchJobAvailable.setChecked(true);
            else
                swtchJobAvailable.setChecked(false);


            btnContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ViewAvailableDoctorsActivity.class);

                    if (currpost.getType().equals("sub")) {
                        intent.putExtra("fromdate", currpost.getDateFrom());
                        intent.putExtra("todate", currpost.getDateTo());
                    } else {
                        intent.putExtra("fromdate", currpost.getDeadline());
                        intent.putExtra("todate", "");
                    }


                    intent.putExtra("placelat", currpost.getPlacelat());
                    intent.putExtra("placelon", currpost.getPlacelon());
                    intent.putExtra("degrees", currpost.getDegree());
                    intent.putExtra("type", currpost.getType());

                    activity.startActivity(intent);
                }
            });

            swtchJobAvailable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItem(i).setAvailable(swtchJobAvailable.isChecked() ? "1" : "0");
                    final ProgressDialog dialog = new ProgressDialog(activity);
                    dialog.setMessage("Status changing. Please wait...");

                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("id", currpost.getId());
                    params.put("type", currpost.getType());
                    params.put("value", swtchJobAvailable.isChecked() ? "1" : "0");
                    client.post(Constants.CHANGE_JOB_AVAILABLE_STATUS, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            dialog.show();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            dialog.dismiss();
                            HelperFunc.showToast(activity, "Successfully changed status");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            dialog.dismiss();
                            HelperFunc.showToast(activity, "Something went wrong.");

                        }
                    });


                }
            });


        } else {
            LinearLayout availableSwitchContainer = v.findViewById(R.id.availableSwitchLayout);
            if (availableSwitchContainer != null)
                ((ViewManager) availableSwitchContainer.getParent()).removeView(availableSwitchContainer);

            btnContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String[] options = new String[]{"Call", "View Facebook Profile"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Pick an option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Dexter.withActivity(activity).withPermission(Manifest.permission.CALL_PHONE).withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currpost.getUser().getPhone()));
                                        activity.startActivity(intent);
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {
                                        Toast.makeText(activity, "Call Permission not granted!", Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                    }
                                }).check();
                            } else {
                                if (!currpost.getUser().getFb_profile().contains("facebook.com")) {
                                    showToast(activity, "Facebook profile_black not available");
                                } else {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currpost.getUser().getFb_profile()));
                                    activity.startActivity(browserIntent);
                                }

                            }
                        }
                    });
                    builder.show();


                }
            });

        }

        return v;
    }

    private void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

}
