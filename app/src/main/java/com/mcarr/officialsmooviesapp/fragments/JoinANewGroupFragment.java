package com.mcarr.officialsmooviesapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.activities.JoinGroupActivity;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.LoadUnJoinedGroupsEvent;
import com.mcarr.officialsmooviesapp.listadapters.GroupsListAdapter;
import com.mcarr.officialsmooviesapp.object.GroupListItem;
import com.mcarr.officialsmooviesapp.object.request.BaseRequest;
import com.mcarr.officialsmooviesapp.object.response.GroupsResponse;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**********************************
 * Created by Mikel on 24-May-16.
 *********************************/
public class JoinANewGroupFragment extends ParentFragment implements AdapterView.OnItemClickListener {

    /** Static **/
    public final static int JOIN_GROUP_REQUEST_CODE = 1;

    /** Views **/
    @BindView(R.id.fragment_join_group_listView) ListView mListView;
    @BindView(R.id.fragment_join_group_progressBar) ProgressBar mProgressBar;

    /** Instance **/
    private GroupsListAdapter mAdapter;
    private ArrayList<GroupListItem> mGroupListItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_join_group, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mAdapter = new GroupsListAdapter(getActivity(), mGroupListItems, false);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mListView.setNestedScrollingEnabled(true);
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
        BaseRequest request = new BaseRequest(Util.getToken(getActivity()));
        BusProvider.getInstance().post(new LoadUnJoinedGroupsEvent.OnLoadingStart(request));
    }

    @Subscribe
    public void onGroupsLoaded(LoadUnJoinedGroupsEvent.OnLoaded onLoaded) {
        GroupsResponse response = onLoaded.getResponse();
        mProgressBar.setVisibility(View.GONE);
        mGroupListItems.clear();
        mGroupListItems.addAll(response.getGroups());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupListItem group = mGroupListItems.get(position);
        Intent intent = new Intent(getActivity(), JoinGroupActivity.class);
        intent.putExtra(JoinGroupActivity.ARGS_GROUP_ID, group.getId());
        intent.putExtra(JoinGroupActivity.ARGS_GROUP_NAME, group.getName());
        getActivity().startActivityForResult(intent, JOIN_GROUP_REQUEST_CODE);
    }
}
