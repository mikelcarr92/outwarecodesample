package com.mcarr.officialsmooviesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.activities.GroupDetailActivity;
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
 * Created by Mikel on 24-May-16.
 *********************************/
public class CreateGroupFragment extends ParentFragment {

    /** Views **/
    @BindView(R.id.fragment_create_a_group_name) EditText mGroupName;
    @BindView(R.id.fragment_create_a_group_password) EditText mPassword;

    /** Instance **/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.activity_create_a_group, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
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
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(Util.getToken(getActivity()), groupName, password);
        BusProvider.getInstance().post(new CreateGroupEvent.OnLoadingStart(createGroupRequest));
    }

    @Subscribe
    public void onGroupsLoaded(CreateGroupEvent.OnLoaded onLoaded) {
        CreateGroupResponse response = onLoaded.getResponse();
        Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
        intent.putExtra(GroupDetailActivity.GROUP_ID, response.getGroupID());
        intent.putExtra(GroupDetailActivity.GROUP_NAME, mGroupName.getText().toString());
        getActivity().finish();
        getActivity().startActivity(intent);
    }

    @Subscribe
    public void onGroupsLoadFailed(CreateGroupEvent.OnLoadingError onLoaded) {
        Toast.makeText(getActivity(), "Create group failed", Toast.LENGTH_SHORT).show();
    }
}
