package com.mcarr.officialsmooviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.CreateGroupEvent;
import com.mcarr.officialsmooviesapp.object.request.CreateGroupRequest;
import com.mcarr.officialsmooviesapp.object.response.CreateGroupResponse;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**********************************
 * Created by Mikel on 07-Jun-16.
 *********************************/
public class CreateGroupActivity extends AppCompatActivity {

    /** Views **/
    @BindView(R.id.fragment_create_a_group_name) EditText mGroupName;
    @BindView(R.id.fragment_create_a_group_password) EditText mPassword;

    /** Instance **/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_a_group);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    @OnClick (R.id.fragment_create_a_group_createButton) void createButtonClick() {
        String groupName = mGroupName.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(groupName)) {
            mGroupName.setError(getString(R.string.error_field_required));
            focusView = mGroupName;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            createGroup(groupName, password);
        }
    }

    private void createGroup(String groupName, String password) {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(Util.getToken(this), groupName, password);
        BusProvider.getInstance().post(new CreateGroupEvent.OnLoadingStart(createGroupRequest));
    }

    @Subscribe
    public void onCreateGroupFinished(CreateGroupEvent.OnLoaded onLoaded) {
        CreateGroupResponse response = onLoaded.getResponse();
        Intent intent = new Intent(this, GroupDetailActivity.class);
        intent.putExtra(GroupDetailActivity.GROUP_ID, response.getGroupID());
        intent.putExtra(GroupDetailActivity.GROUP_NAME, mGroupName.getText().toString());
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    @Subscribe
    public void onCreateGroupFailed(CreateGroupEvent.OnLoadingError onLoaded) {
        Toast.makeText(this, "Create group failed", Toast.LENGTH_SHORT).show();
    }
}
