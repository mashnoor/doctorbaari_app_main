package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doctorbaari.android.R;
import com.doctorbaari.android.models.User;
import com.doctorbaari.android.utils.CommonDialog;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.Geson;
import com.doctorbaari.android.utils.SideNToolbarController;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

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

    @BindView(R.id.login_button)
    LoginButton loginButton;

    @BindView(R.id.profile_image)
    CircleImageView profileImage;


    AsyncHttpClient client;
    ProgressDialog dialog;
    CallbackManager callbackManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Logger.d(data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        SideNToolbarController.attach(this, "Your Profile");
        Logger.addLogAdapter(new AndroidLogAdapter());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.doctorbaari.android",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", hash);
                //CommonDialog.showDialog(this, hash);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");


        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));


        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.d(loginResult.getRecentlyGrantedPermissions());
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    Logger.d(object);
                                    String email = object.optString("email", "Not Available");
                                    String profileLink = object.optString("link", "Not Available");
                                    String imageLink = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    updateToServer(email, profileLink, imageLink);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // 01/31/1980 format
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,link,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        RequestParams params = new RequestParams();
        params.put("userid", DBHelper.getUserid(this));
        client.post(Constants.GET_PROFILE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Logger.d(DBHelper.getUserid(ProfileActivity.this));
                Logger.d(response);
                User user = Geson.g().fromJson(response, User.class);
                Logger.d(user);
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


                Glide.with(ProfileActivity.this).load(user.getPpUrl()).apply(requestOptions).into(profileImage);
                dialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Logger.d("error");
                dialog.dismiss();

            }
        });


    }


    private void showToast(String s) {
        Toast.makeText(ProfileActivity.this, s, Toast.LENGTH_LONG).show();
    }

    private void updateToServer(final String email, String profileLink, String imageLink) {
        RequestParams params = new RequestParams();
        params.put("userid", DBHelper.getUserid(ProfileActivity.this));
        params.put("email", email);
        params.put("link", profileLink);
        params.put("picture_url", imageLink);
        client.post(Constants.UPDATE_PROFILE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
                Logger.d(DBHelper.getUserid(ProfileActivity.this));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                showToast("Conncected with facebook successfully");
                recreate();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                dialog.dismiss();
                showToast("Something went wrong");
                Logger.d(error.getMessage());
                Logger.d(new String(responseBody));

            }
        });
    }


    public void goViewAllWorkingLocations(View v) {
        startActivity(new Intent(this, AllWorkLocationsActivity.class));
    }


    public void goAvaibilityActivity(View v) {
        Intent i = new Intent(this, AvaibilityListActivity.class);
        i.putExtra("type", "all");
        startActivity(i);
    }

    public void goViewHistory(View view) {
        Intent i = new Intent(this, HistoryActivity.class);
        i.putExtra("type", "all");
        startActivity(i);
    }
}
