package com.mcarr.officialsmooviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.DisbandGroupEvent;
import com.mcarr.officialsmooviesapp.events.GetGroupDetailsEvent;
import com.mcarr.officialsmooviesapp.events.GetMembersForGroupEvent;
import com.mcarr.officialsmooviesapp.events.LeaveGroupEvent;
import com.mcarr.officialsmooviesapp.fragments.GroupEventsFragment;
import com.mcarr.officialsmooviesapp.fragments.GroupMembersFragment;
import com.mcarr.officialsmooviesapp.object.Group;
import com.mcarr.officialsmooviesapp.object.request.GroupRequest;
import com.mcarr.officialsmooviesapp.object.request.LeaveGroupRequest;
import com.mcarr.officialsmooviesapp.object.response.GetGroupDetailsResponse;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**********************************
 * Created by Mikel on 25-May-16.
 *********************************/
public class GroupDetailActivity extends AppCompatActivity {

    /** Static **/
    public static final String GROUP_NAME = "GROUP_NAME";
    public static final String GROUP_ID = "GROUP_ID";
    public static final String GROUP_IMAGE = "GROUP_IMAGE";

    /** Views **/
    @BindView(R.id.activity_group_detail_viewpager) ViewPager mViewPager;
    @BindView(R.id.activity_group_detail_tabs) TabLayout mTabLayout;
    @BindView(R.id.activity_group_collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.activity_groupu_detail_app_bar_layout) AppBarLayout mAppBarLayout;
    @BindView(R.id.activity_group_detail_tab_background) View mTabBackground;
    @BindView(R.id.activity_group_detail_imageView) ImageView mBackground;
    @BindView(R.id.activity_group_detail_toolbar) Toolbar mToolbar;

    /** Instance **/
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private int mGroupID;
    private String mGroupName;
    private Boolean mIsAdmin;
    private String mGroupImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mCollapsingToolbar.setTitleEnabled(false);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            mGroupName = getIntent().getStringExtra(GROUP_NAME);
            mGroupID = getIntent().getIntExtra(GROUP_ID, -1);
            mGroupImage = getIntent().getStringExtra(GROUP_IMAGE);
        }

        if (mGroupImage != null) {
            Picasso.with(this)
                   .load(mGroupImage)
                   .into(mBackground);
        }

        setTitle(mGroupName);

        mFragments.add(GroupMembersFragment.newInstance(mGroupID));
        mFragments.add(GroupEventsFragment.newInstance(mGroupID));

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(sectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);

        AppBarLayout.OnOffsetChangedListener listener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(mCollapsingToolbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(mCollapsingToolbar)) {
                    mTabBackground.animate().alpha(0).setDuration(600);
                } else {
                    mTabBackground.animate().alpha(1).setDuration(600);
                }
            }
        };

        mAppBarLayout.addOnOffsetChangedListener(listener);
    }

    @OnClick(R.id.activity_group_detail_fab) void onCreateEvent() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        intent.putExtra(CreateEventActivity.ARGS_GROUP_ID, mGroupID);
        intent.putExtra(CreateEventActivity.ARGS_GROUP_IMAGE, mGroupImage);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_activity_group_leave_group:
                if (mIsAdmin == null) { return true; }
                if (mIsAdmin) { // Disband group
                    GroupRequest request = new GroupRequest(Util.getToken(this), mGroupID);
                    BusProvider.getInstance().post(new DisbandGroupEvent.OnLoadingStart(request));
                } else {        // Leave group
                    LeaveGroupRequest request = new LeaveGroupRequest(mGroupID, Util.getToken(this));
                    BusProvider.getInstance().post(new LeaveGroupEvent.OnLoadingStart(request));
                }
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
        GroupRequest tokenRequest = new GroupRequest(Util.getToken(this), mGroupID);
        BusProvider.getInstance().post(new GetGroupDetailsEvent.OnLoadingStart(tokenRequest));
    }

    @Subscribe
    public void onGroupLoaded(GetGroupDetailsEvent.OnLoaded onLoaded) {

        GetGroupDetailsResponse response = onLoaded.getResponse();
        Group group = response.getGroup();

        if (group != null) {
            mIsAdmin = group.isAdmin();
            if (mGroupImage == null) {
                Picasso.with(this).load(group.getGroupImage()).into(mBackground);
            }
        }
    }

    @Subscribe
    public void onGroupFailed(GetGroupDetailsEvent.OnLoadingError onLoadingFailed) {
        Toast.makeText(this, "load group details failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_group_detail, menu);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Members";
                case 1:
                    return "Events";
            }
            return null;
        }
    }

    @Subscribe
    public void leaveGroupDone(LeaveGroupEvent.OnLoaded onLoaded) {
        finish();
    }

    @Subscribe
    public void leaveGroupFailed(GetMembersForGroupEvent.OnLoadingError onLoadingFailed) {
        Toast.makeText(this, "leave group failed", Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void disbandGroupDone(DisbandGroupEvent.OnLoaded onLoaded) {
        finish();
    }

    @Subscribe
    public void disbandGroupFailed(DisbandGroupEvent.OnLoadingError onLoadingError) {
        Toast.makeText(this, "disband group failed", Toast.LENGTH_LONG).show();
    }
}