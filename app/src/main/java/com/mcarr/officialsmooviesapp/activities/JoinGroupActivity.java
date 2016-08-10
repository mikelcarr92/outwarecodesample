package com.mcarr.officialsmooviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.JoinGroupEvent;
import com.mcarr.officialsmooviesapp.object.request.JoinGroupRequest;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**********************************
 * Created by Mikel on 07-Jun-16.
 *********************************/
public class JoinGroupActivity extends AppCompatActivity {

    /** Static **/
    public final static int JOIN_GROUP_REQUEST_CODE = 1;
    public static final String ARGS_GROUP_NAME = "ARGS_GROUP_NAME";
    public static final String ARGS_GROUP_ID = "ARGS_GROUP_ID";

    /** Views **/
    @BindView (R.id.activity_join_group_group_name) TextView mGroupNameView;
    @BindView (R.id.activity_join_group_password) EditText mPasswordView;

    /** Instance **/
    private int mGroupID = -1;
    private String mGroupName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent() != null) {
            mGroupID = getIntent().getIntExtra(ARGS_GROUP_ID, -1);
            mGroupName = getIntent().getStringExtra(ARGS_GROUP_NAME);
        }
        mGroupNameView.setText(mGroupName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @OnClick(R.id.activity_join_group_join_button) void joinButtonPressed() {

        String password = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
            return;
        }

        JoinGroupRequest tokenRequest = new JoinGroupRequest(mGroupID, Util.getToken(this), password);
        BusProvider.getInstance().post(new JoinGroupEvent.OnLoadingStart(tokenRequest));
    }

    @Subscribe
    public void onJoinGroupSuccess(JoinGroupEvent.OnLoaded onLoaded) {
        Intent intent = new Intent(this, GroupDetailActivity.class);
        intent.putExtra(GroupDetailActivity.GROUP_ID, mGroupID);
        intent.putExtra(GroupDetailActivity.GROUP_NAME, mGroupName);
        startActivity(intent);

        setResult(RESULT_OK);
        finish();
    }

    @Subscribe
    public void onJoinGroupFailed(JoinGroupEvent.OnLoadingError onLoadingFailed) {
        // TODO: Check error message in case the password was right but something went wrong.
        mPasswordView.setError(getString(R.string.error_wrong_password));
        mPasswordView.requestFocus();
    }
}
