package com.mcarr.officialsmooviesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.activities.CreateEventActivity;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.DisbandGroupEvent;
import com.mcarr.officialsmooviesapp.events.GetGroupDetailsEvent;
import com.mcarr.officialsmooviesapp.events.GetMembersForGroupEvent;
import com.mcarr.officialsmooviesapp.events.LeaveGroupEvent;
import com.mcarr.officialsmooviesapp.object.request.GroupRequest;
import com.mcarr.officialsmooviesapp.object.request.LeaveGroupRequest;
import com.mcarr.officialsmooviesapp.object.response.GetGroupDetailsResponse;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

/**********************************
 * Created by Mikel on 07-Jun-16.
 *********************************/
public class GroupDetailsFragment extends ParentFragment {

    /** Static **/
    private static final String ARGS_GROUP_ID = "ARGS_GROUP_ID";

    /** Views **/
    @BindView(R.id.fragment_group_details_progressBar) ProgressBar mProgressBar;
    @BindView(R.id.fragment_group_details_content) LinearLayout mContent;
    @BindView(R.id.fragment_group_details_created_text) TextView mCreatedText;
    @BindView(R.id.fragment_group_details_leaveButton) Button mLeaveButton;

    /** Instance **/
    @State protected int mGroupID;
    @State protected boolean mIsAdmin;

    public static GroupDetailsFragment newInstance(int groupID) {
        GroupDetailsFragment fragment = new GroupDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_GROUP_ID, groupID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_group_details, container, false);
        Icepick.restoreInstanceState(this, savedInstanceState);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @OnClick(R.id.fragment_group_details_leaveButton) protected void onLeaveClick() {
        if (mIsAdmin) { // Disband group
            GroupRequest request = new GroupRequest(Util.getToken(getActivity()), mGroupID);
            BusProvider.getInstance().post(new DisbandGroupEvent.OnLoadingStart(request));
        } else { // Leave group
            LeaveGroupRequest request = new LeaveGroupRequest(mGroupID, Util.getToken(getActivity()));
            BusProvider.getInstance().post(new LeaveGroupEvent.OnLoadingStart(request));
        }
    }

    @OnClick(R.id.fragment_group_details_createEvent) protected void onCreateEventClick() {
        Intent intent = new Intent(getActivity(), CreateEventActivity.class);
        intent.putExtra(CreateEventActivity.ARGS_GROUP_ID, mGroupID);
        getActivity().startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (getArguments() != null) {
            mGroupID = getArguments().getInt(ARGS_GROUP_ID, -1);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        GroupRequest tokenRequest = new GroupRequest(Util.getToken(getActivity()), mGroupID);
        BusProvider.getInstance().post(new GetGroupDetailsEvent.OnLoadingStart(tokenRequest));
    }

    @Subscribe
    public void onGroupLoaded(GetGroupDetailsEvent.OnLoaded onLoaded) {
        GetGroupDetailsResponse response = onLoaded.getResponse();

        if (response.getGroup() != null) {
            mIsAdmin = response.getGroup().isAdmin();
        }

        mLeaveButton.setText(mIsAdmin ? "Disband Group" : "Leave Group");
        mProgressBar.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);
        mCreatedText.setText("A group since " + response.getGroup().getDateCreated());
    }

    @Subscribe
    public void onGroupFailed(GetGroupDetailsEvent.OnLoadingError onLoadingFailed) {
        Toast.makeText(getActivity(), "load failed", Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void leaveGroupDone(LeaveGroupEvent.OnLoaded onLoaded) {
        getActivity().finish();
    }

    @Subscribe
    public void leaveGroupFailed(GetMembersForGroupEvent.OnLoadingError onLoadingFailed) {
        Toast.makeText(getActivity(), "load failed", Toast.LENGTH_LONG).show();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Subscribe
    public void disbandGroupDone(DisbandGroupEvent.OnLoaded onLoaded) {
        getActivity().finish();
    }

    @Subscribe
    public void disbandGroupFailed(DisbandGroupEvent.OnLoadingError onLoadingError) {
        Toast.makeText(getActivity(), "disband group failed", Toast.LENGTH_LONG).show();

    }
}