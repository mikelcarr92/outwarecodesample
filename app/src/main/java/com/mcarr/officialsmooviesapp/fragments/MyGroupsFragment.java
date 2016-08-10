package com.mcarr.officialsmooviesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.activities.AddGroupActivity;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.listadapters.MyGroupsRecyclerAdapter;
import com.mcarr.officialsmooviesapp.object.GroupListItem;
import com.mcarr.officialsmooviesapp.object.request.BaseRequest;
import com.mcarr.officialsmooviesapp.object.response.GroupsResponse;
import com.mcarr.officialsmooviesapp.events.LoadMyGroupsEvent;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class MyGroupsFragment extends ParentFragment {

    /** Views **/
    @BindView(R.id.fragment_my_groups_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.fragment_my_groups_progressBar) ProgressBar mProgressBar;

    /** Instance **/
    //private GroupsListAdapter mAdapter;
    private MyGroupsRecyclerAdapter mAdapter;
    private ArrayList<GroupListItem> mListItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((DaggerApplication) getActivity().getApplication()).getNetComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_groups, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
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
        BusProvider.getInstance().post(new LoadMyGroupsEvent.OnLoadingStart(request));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mAdapter = new MyGroupsRecyclerAdapter(mListItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnGroupClickListener(new MyGroupsRecyclerAdapter.OnMyGroupClickListener() {
//            @Override
//            public void onGroupClick(GroupListItem groupListItem, ImageView imageView) {
//                Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
//                intent.putExtra(GroupDetailActivity.GROUP_NAME, groupListItem.getName());
//                intent.putExtra(GroupDetailActivity.GROUP_ID, groupListItem.getId());
//                startActivity(intent);
//            }
//        });
    }

    @OnClick (R.id.activity_my_groups_fab) void fabClick() {
        Intent intent = new Intent(getActivity(), AddGroupActivity.class);
        startActivity(intent);
    }

    @Subscribe
    public void onGroupsLoaded(LoadMyGroupsEvent.OnLoaded onLoaded) {
        GroupsResponse response = onLoaded.getResponse();
        mProgressBar.setVisibility(View.GONE);
        mListItems.clear();
        mListItems.addAll(response.getGroups());
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onGroupsFailed(LoadMyGroupsEvent.OnLoadingError onLoadingFailed) {
        Toast.makeText(getActivity(), "load failed", Toast.LENGTH_LONG).show();
    }
}