package com.doctorbaari.android.acvities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.doctorbaari.android.R;
import com.doctorbaari.android.utils.Constants;
import com.doctorbaari.android.utils.DBHelper;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    int APP_REQUEST_CODE = 126;

    ProgressDialog dialog;


    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        client = new AsyncHttpClient();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to server...");
        if (DBHelper.isSignedIn(this)) {
            startActivity(new Intent(this, NewsfeedActivity.class));
            finish();
        }


    }

    public void goLogin(View v) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);

    }

    public void goRegistration(View v) {
        startActivity(new Intent(this, WelcomeActivity.class));
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null) {


            } else if (loginResult.wasCancelled()) {


            } else {

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {

                        String phoneNumber = account.getPhoneNumber().toString();
                        RequestParams params = new RequestParams();
                        params.put("phone", phoneNumber);
                        client.post(Constants.VERIFY_IF_NUMBER_ADDED, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                dialog.show();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String response = new String(responseBody);
                                if (response.equals("false")) {
                                    dialog.dismiss();
                                    showToast("Account is not found. Please Sign Up");

                                } else {
                                    finish();
                                    DBHelper.setUserId(LoginActivity.this, response);
                                    DBHelper.setSignedInStatus(LoginActivity.this, true);
                                    startActivity(new Intent(LoginActivity.this, NewsfeedActivity.class));
                                }


                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                dialog.dismiss();
                                showToast("Something went wrong");

                            }
                        });


                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // Handle Error
                    }
                });

            }

            // If you have an authorization code, retrieve it from
            // loginResult.getAuthorizationCode()
            // and pass it to your server and exchange it for an access token.

            // Success! Start your next activity...

        }

        // Surface the result to your user in an appropriate way.


    }
}

