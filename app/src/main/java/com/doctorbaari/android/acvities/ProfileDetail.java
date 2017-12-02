package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doctorbaari.android.R;
import com.doctorbaari.android.models.User;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetail extends AppCompatActivity {

    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvInstitue)
    TextView tvInstitue;
    @BindView(R.id.tvCurrentlyWorking)
    TextView tvCurrentlyWorking;
    @BindView(R.id.tvDegree)
    TextView tvDegree;
    @BindView(R.id.tvDateOfBirth)
    TextView tvDateOfBirth;
    @BindView(R.id.tvFbLink)
    TextView tvFbLink;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvMobile)
    TextView tvMobile;



    @BindView(R.id.profile_image)
    CircleImageView profileImage;


    AsyncHttpClient client;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "User Profile");
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");

        Intent i = getIntent();
        String userid = i.getStringExtra("userid");
        RequestParams params = new RequestParams();
        params.put("userid", userid);
        client.post(Constants.GET_PROFILE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);

                Logger.d(response);
                User user = Geson.g().fromJson(response, User.class);
                Logger.d(user);
                tvUserName.setText(user.getUsername());
                tvInstitue.setText("Medical College: " + user.getCollege());
                tvCurrentlyWorking.setText("Currently Working: " + user.getPlace());
                tvDegree.setText("Degree: " + user.getDegree());
                tvDateOfBirth.setText("Date of Birth:" + user.getBirthdate());
                tvFbLink.setText("Facebook Profle: " + user.getFb_profile());
                tvEmail.setText("Email: " + user.getEmail());
                tvMobile.setText("Phone: " + user.getPhone());

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.profile);
                requestOptions.error(R.drawable.profile);


                Glide.with(ProfileDetail.this).load(user.getPpUrl()).apply(requestOptions).into(profileImage);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Logger.d("error");
                dialog.dismiss();

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
