package com.doctorbaari.android.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doctorbaari.android.R;
import com.doctorbaari.android.acvities.JobDetailsActivity;
import com.doctorbaari.android.models.DoctorSub;
import com.doctorbaari.android.utils.HelperFunc;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nowfel Mashnoor on 12/1/2017.
 */

public class DoctorAdapter extends BaseAdapter {
    private Activity activity;
    private DoctorSub[] doctorSubs;

    public DoctorAdapter(Activity activity, DoctorSub[] doctorSubs) {
        this.activity = activity;
        this.doctorSubs = doctorSubs;
    }

    @Override
    public int getCount() {
        return doctorSubs.length;
    }

    @Override
    public DoctorSub getItem(int i) {
        return doctorSubs[i];
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
            v = layoutInflater.inflate(R.layout.row_doctor, null);
        }
        CircleImageView ivImage = v.findViewById(R.id.ivProfileImage);
        TextView tvUserName = v.findViewById(R.id.tvUserName);
        TextView tvCollege = v.findViewById(R.id.tvCollege);
        TextView tvDegree = v.findViewById(R.id.tvDegree);
        TextView tvLocation = v.findViewById(R.id.tvLocation);
        TextView tvDistance = v.findViewById(R.id.tvDistance);

        Button btnLocation = v.findViewById(R.id.btnLocation);

        Button btnContact = v.findViewById(R.id.btnContact);


        final DoctorSub currentDoctorSub = getItem(i);

        //Onclick For Facebook Icon
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = new String[]{"Call", "View Facebook Profile"};
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Pick a option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Dexter.withActivity(activity).withPermission(Manifest.permission.CALL_PHONE).withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentDoctorSub.getPhone()));
                                    activity.startActivity(intent);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {
                                    HelperFunc.showToast(activity, "Call permission not granted!");

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                }
                            }).check();
                        } else {
                            if (!currentDoctorSub.getFbProfile().contains("facebook.com")) {
                                HelperFunc.showToast(activity, "Facebook profile not available");
                            } else {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentDoctorSub.getFbProfile()));
                                activity.startActivity(browserIntent);
                            }

                        }
                    }
                });
                builder.show();

            }
        });




        //On Click Place
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:0,0?q=" + (currentDoctorSub.getPlacelat() + ", " + currentDoctorSub.getPlacelon())));
                try {
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.profile);
        requestOptions.error(R.drawable.profile);

        Glide.with(activity).load(currentDoctorSub.getPpUrl()).apply(requestOptions).into(ivImage);
        tvUserName.setText(currentDoctorSub.getUsername());
        tvCollege.setText("College: " + currentDoctorSub.getCollege());
        tvDegree.setText("Degree: " + currentDoctorSub.getDegree());
        tvLocation.setText("Location: " + currentDoctorSub.getPlace());
        tvDistance.setText("Distance: " + currentDoctorSub.getDistance());


        return v;
    }
}
