package com.mcarr.officialsmooviesapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.LoadUnJoinedGroupsEvent;
import com.mcarr.officialsmooviesapp.fragments.JoinANewGroupFragment;
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
 * Created by Mikel on 23-May-16.
 *********************************/
public class AddGroupActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /** Views **/
    @BindView(R.id.fragment_join_group_listView)
    ListView mListView;
    @BindView(R.id.fragment_join_group_progressBar)
    ProgressBar mProgressBar;

    /** Instance **/
    private GroupsListAdapter mAdapter;
    private ArrayList<GroupListItem> mGroupListItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new GroupsListAdapter(AddGroupActivity.this, mGroupListItems, false);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mListView.setNestedScrollingEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_activity_add_group:
                createGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_add_group, menu);
        return true;
    }

    private void createGroup() {
        Intent intent = new Intent(this, CreateGroupActivity.class);
        startActivityForResult(intent, JoinGroupActivity.JOIN_GROUP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == JoinANewGroupFragment.JOIN_GROUP_REQUEST_CODE)
            if (resultCode == RESULT_OK)
                finish();
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
        Intent intent = new Intent(AddGroupActivity.this, JoinGroupActivity.class);
        intent.putExtra(JoinGroupActivity.ARGS_GROUP_ID, group.getId());
        intent.putExtra(JoinGroupActivity.ARGS_GROUP_NAME, group.getName());
        startActivityForResult(intent, JoinGroupActivity.JOIN_GROUP_REQUEST_CODE);
    }
}