package com.mcarr.officialsmooviesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.messaging.FirebaseMessaging;
import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.object.response.LoginResponse;
import com.mcarr.officialsmooviesapp.util.Constants;
import com.mcarr.officialsmooviesapp.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class MainLoginActivity extends AppCompatActivity {

    //@Inject SharedPreferences sharedPreferences;
    //@Inject ISmooviesAPI ISmooviesAPI;

    /** Views **/
    @BindView(R.id.activity_login_email) EditText mEmailView;
    @BindView(R.id.activity_login_password) EditText mPasswordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_two);
        ButterKnife.bind(this);
        FirebaseMessaging.getInstance().subscribeToTopic("groups");
        //((DaggerApplication) getApplication()).getNetComponent().inject(this);
    }

    @OnClick(R.id.activity_login_dologin_button)
    protected void attemptLogin() {

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            requestLogin(email, password);
        }
    }

    private void requestLogin(String email, String password) {

        Call<LoginResponse> call = Util.getApi().login(email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.successful) {
                        //Log.i("DEV9", "Login successful");
                        //Log.i("DEV9", "token: " + loginResponse.getToken());
                        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(MainLoginActivity.this).edit();

                        //SharedPreferences.Editor prefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).edit();
                        //SharedPreferences.Editor prefs = sharedPreferences.edit();
                        prefs.putString(Constants.AUTH_KEY, loginResponse.getToken());
                        prefs.putString(Constants.USERNAME, loginResponse.getName());
                        prefs.putString(Constants.PROFILE_URL, loginResponse.getProfileImage());
                        prefs.apply();

                        Intent intent = new Intent(MainLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Already exists
                    }
                } else {
                    // Weird error
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Server/Connection error
            }
        });
    }

    @OnClick(R.id.activity_login_create_account_button)
    protected void createAccount() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }
}
