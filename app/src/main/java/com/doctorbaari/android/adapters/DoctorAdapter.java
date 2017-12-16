package com.doctorbaari.android.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doctorbaari.android.R;
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

        ImageView ivFacebook = v.findViewById(R.id.ivFb);
        ImageView ivCall = v.findViewById(R.id.ivCall);
        ImageView ivPlace = v.findViewById(R.id.ivPlace);

        final DoctorSub currentDoctorSub = getItem(i);

        //Onclick For Facebook Icon
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentDoctorSub.getFbProfile().contains("facebook.com")) {
                    HelperFunc.showToast(activity, "Facebook profile not available");
                } else {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentDoctorSub.getFbProfile()));
                    activity.startActivity(browserIntent);
                }

            }
        });

        //On Click Call
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(activity).withPermission(Manifest.permission.CALL_PHONE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentDoctorSub.getPhone()));
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
            }
        });

        //On Click Place
        ivPlace.setOnClickListener(new View.OnClickListener() {
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


        return v;
    }
}
