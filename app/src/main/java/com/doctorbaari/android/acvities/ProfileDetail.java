package com.doctorbaari.android.acvities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doctorbaari.android.R;
import com.doctorbaari.android.models.User;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.HelperFunc;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


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

    @BindView(R.id.tvFbLink)
    TextView tvFbLink;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvMobile)
    TextView tvMobile;
    @BindView(R.id.etReview)
    EditText etReview;
    @BindView(R.id.rbReview)
    RatingBar rbReview;


    @BindView(R.id.profile_image)
    CircleImageView profileImage;


    AsyncHttpClient client;
    ProgressDialog dialog;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "User Profile");
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");
        loadUserProfile();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void loadUserProfile()

    {

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


                user = Geson.g().fromJson(response, User.class);

                tvUserName.setText(user.getUsername());
                tvInstitue.setText(user.getCollege());
                tvCurrentlyWorking.setText(user.getPlace());
                tvDegree.setText(user.getDegree());

                tvFbLink.setText(user.getFb_profile());
                tvEmail.setText(user.getEmail());
                tvMobile.setText(user.getPhone());

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.profile_black);
                requestOptions.error(R.drawable.profile_black);


                Glide.with(ProfileDetail.this).load(user.getPpUrl()).apply(requestOptions).into(profileImage);
                dialog.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                dialog.dismiss();

            }
        });
    }

    public void goViewAllWorkingLocations(View v) {
        Intent i = new Intent(this, AllWorkLocationsOtherUserActivity.class);
        i.putExtra("userid", getIntent().getStringExtra("userid"));
        startActivity(i);
    }

    public void postReview(View v) {
        Intent i = getIntent();
        String reviewed_to = i.getStringExtra("userid");
        String review = etReview.getText().toString();
        String reviewed_from = DBHelper.getUserid(ProfileDetail.this);
        String rating = String.valueOf(rbReview.getRating());
        RequestParams params = new RequestParams();
        params.put("reviewed_from", reviewed_from);
        params.put("reviewed_to", reviewed_to);
        params.put("review", review);
        params.put("rating", rating);
        client.post(Constants.POST_A_REVIEW, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                String response = new String(responseBody);
                if (response.equals("already")) {
                    HelperFunc.showToast(ProfileDetail.this, "You have already reviewed once!");

                } else {
                    HelperFunc.showToast(ProfileDetail.this, "Successfully Posted Review");
                }

                etReview.setText("");
                rbReview.setRating(0f);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                HelperFunc.showToast(ProfileDetail.this, "Something went wrong");
                dialog.dismiss();


            }
        });


    }

    public void viewReviews(View v) {
        Intent i = new Intent(this, Reviews.class);
        i.putExtra("userid", getIntent().getStringExtra("userid"));
        startActivity(i);
    }

    public void goContact(View v) {
        String[] options = new String[]{"Call", "View Facebook Profile"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileDetail.this);
        builder.setTitle("Pick a option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Dexter.withActivity(ProfileDetail.this).withPermission(Manifest.permission.CALL_PHONE).withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.getPhone()));
                            startActivity(intent);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(ProfileDetail.this, "Call Permission not granted!", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }).check();
                } else {
                    if (!user.getFb_profile().contains("facebook.com")) {
                        HelperFunc.showToast(ProfileDetail.this, "Facebook profile_black not available");
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(user.getFb_profile()));
                        startActivity(browserIntent);
                    }

                }
            }
        });
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SideNToolbarController.closeDrawer();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


}
