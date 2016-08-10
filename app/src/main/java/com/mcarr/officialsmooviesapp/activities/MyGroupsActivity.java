package com.mcarr.officialsmooviesapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.LoadMyGroupsEvent;
import com.mcarr.officialsmooviesapp.listadapters.MyGroupsRecyclerAdapter;
import com.mcarr.officialsmooviesapp.object.GroupListItem;
import com.mcarr.officialsmooviesapp.object.request.BaseRequest;
import com.mcarr.officialsmooviesapp.object.response.GroupsResponse;
import com.mcarr.officialsmooviesapp.util.Constants;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**********************************
 * Created by Mikel on 23-Jun-16.
 *********************************/
public class MyGroupsActivity extends AppCompatActivity {

    /** Views **/
    @BindView(R.id.fragment_my_groups_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.fragment_my_groups_progressBar) ProgressBar mProgressBar;

    /** Instance **/
    private MyGroupsRecyclerAdapter mAdapter;
    private ArrayList<GroupListItem> mListItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_groups);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new MyGroupsRecyclerAdapter(mListItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnGroupClickListener(new MyGroupsRecyclerAdapter.OnMyGroupClickListener() {
            @Override
            public void onGroupClick(GroupListItem groupListItem, ImageView imageView, TextView name) {
                Intent intent = new Intent(MyGroupsActivity.this, GroupDetailActivity.class);
                intent.putExtra(GroupDetailActivity.GROUP_NAME, groupListItem.getName());
                intent.putExtra(GroupDetailActivity.GROUP_ID, groupListItem.getId());
                intent.putExtra(GroupDetailActivity.GROUP_IMAGE, groupListItem.getGroupImage());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MyGroupsActivity.this, imageView, "groupImage");
//                Pair<View, String> p1 = Pair.create((View)imageView, "groupImage");
//                Pair<View, String> p2 = Pair.create((View)name, "name");
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MyGroupsActivity.this, p1, p2);
                startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        BaseRequest request = new BaseRequest(Util.getToken(this));
        BusProvider.getInstance().post(new LoadMyGroupsEvent.OnLoadingStart(request));
    }

    @OnClick(R.id.activity_my_groups_fab) void fabClick() {
        Intent intent = new Intent(this, AddGroupActivity.class);
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
        Toast.makeText(this, "load failed", Toast.LENGTH_LONG).show();
    }
}