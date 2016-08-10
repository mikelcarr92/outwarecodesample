package com.mcarr.officialsmooviesapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.GetMembersForGroupEvent;
import com.mcarr.officialsmooviesapp.listadapters.MembersListAdapter;
import com.mcarr.officialsmooviesapp.object.Member;
import com.mcarr.officialsmooviesapp.object.request.GroupRequest;
import com.mcarr.officialsmooviesapp.object.response.GetGroupMembersResponse;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**********************************
 * Created by Mikel on 07-Jun-16.
 *********************************/
public class GroupMembersFragment extends ParentFragment {

    /** Static **/
    private static final String ARGS_GROUP_ID = "ARGS_GROUP_ID";

    /** Views **/
    @BindView(R.id.fragment_group_members_progressBar) ProgressBar mProgressBarView;
    @BindView(R.id.fragment_group_members_listView) ListView mListView;

    /** Instance **/
    private MembersListAdapter mAdapter;
    private ArrayList<Member> mListItems = new ArrayList<>();
    private int mGroupID;

    public static GroupMembersFragment newInstance(int groupID) {
        GroupMembersFragment fragment = new GroupMembersFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_GROUP_ID, groupID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_group_members, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (getArguments() != null) {
            mGroupID = getArguments().getInt(ARGS_GROUP_ID, -1);
        }
        mAdapter = new MembersListAdapter(getActivity(), mListItems);
        mListView.setAdapter(mAdapter);
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
        BusProvider.getInstance().post(new GetMembersForGroupEvent.OnLoadingStart(tokenRequest));
    }

    @Subscribe
    public void onGroupLoaded(GetMembersForGroupEvent.OnLoaded onLoaded) {
        GetGroupMembersResponse response = onLoaded.getResponse();
        mListItems.clear();
        mListItems.addAll(response.getMembers());
        mAdapter.notifyDataSetChanged();
        mProgressBarView.setVisibility(View.GONE);
    }

    @Subscribe
    public void onGroupFailed(GetMembersForGroupEvent.OnLoadingError onLoadingFailed) {
        mProgressBarView.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "load failed", Toast.LENGTH_LONG).show();
    }
}
