package com.mcarr.officialsmooviesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.object.response.LoginResponse;
import com.mcarr.officialsmooviesapp.retrofit.ISmooviesAPI;
import com.mcarr.officialsmooviesapp.util.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class CreateAccountActivity extends AppCompatActivity {

    /** TODO: Broken because of dagger **/

    @Inject SharedPreferences sharedPreferences;
    @Inject ISmooviesAPI ISmooviesAPI;

    @BindView(R.id.activity_create_account_username) EditText mUsernameView;
    @BindView(R.id.activity_create_account_email) EditText mEmailView;
    @BindView(R.id.activity_create_account_password) EditText mPasswordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_create_account_register_button)
    protected void attemptRegister() {
        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
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

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
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
            requestCreateAccount(username, email, password);
        }
    }

    private void requestCreateAccount(String userName, String email, String password) {

        Call<LoginResponse> call = ISmooviesAPI.createAccount(userName, email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.successful) {

                        SharedPreferences.Editor prefs = sharedPreferences.edit();
                        prefs.putString(Constants.AUTH_KEY, loginResponse.getToken());
                        prefs.putString(Constants.USERNAME, loginResponse.getName());
                        prefs.putString(Constants.PROFILE_URL, null);
                        prefs.apply();

                        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @OnClick(R.id.activity_create_account_existing_account_button)
    protected void backToLogin() {
        Intent intent = new Intent(this, MainLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
