package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.doctorbaari.android.utils.HelperFunc;
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
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class ConnectWithFacebookActivity extends AppCompatActivity {

    @BindView(R.id.login_button)
    LoginButton loginButton;

    CallbackManager callbackManager;
    AsyncHttpClient client;
    ProgressDialog dialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Logger.d(data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_with_facebook);

        ButterKnife.bind(this);
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


    }

    private void updateToServer(final String email, String profileLink, String imageLink) {
        RequestParams params = new RequestParams();
        params.put("userid", DBHelper.getUserid(ConnectWithFacebookActivity.this));
        params.put("email", email);
        params.put("link", profileLink);
        params.put("picture_url", imageLink);
        client.post(Constants.UPDATE_PROFILE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
                Logger.d(DBHelper.getUserid(ConnectWithFacebookActivity.this));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                dialog.dismiss();
                startActivity(new Intent(ConnectWithFacebookActivity.this, HomeActitvity.class));
                HelperFunc.showToast(ConnectWithFacebookActivity.this, "Conncected with facebook successfully");
                finish();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                dialog.dismiss();
                HelperFunc.showToast(ConnectWithFacebookActivity.this, "Something went wrong");
                Logger.d(error.getMessage());
                Logger.d(new String(responseBody));
                finish();

            }
        });
    }



    public void goSkip(View v)
    {
        startActivity(new Intent(this, HomeActitvity.class));
        finish();
    }
}
