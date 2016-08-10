package com.mcarr.officialsmooviesapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.CancelEventEvent;
import com.mcarr.officialsmooviesapp.events.GetEventDetailEvent;
import com.mcarr.officialsmooviesapp.events.GetMembersForGroupEvent;
import com.mcarr.officialsmooviesapp.listadapters.EventDetailsRecyclerAdapter;
import com.mcarr.officialsmooviesapp.object.Attendee;
import com.mcarr.officialsmooviesapp.object.Event;
import com.mcarr.officialsmooviesapp.object.request.CancelEventRequest;
import com.mcarr.officialsmooviesapp.object.request.EventRequest;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

/**********************************
 * Created by Mikel on 06-Jun-16.
 *********************************/
public class EventDetailActivity extends AppCompatActivity {

    /** Static **/
    public static final String ARGS_EVENT_ID = "ARGS_EVENT_ID";
    public static final String ARGS_EVENT_NAME = "ARGS_EVENT_NAME";
    public static final String ARGS_GROUP_IMAGE = "ARGS_GROUP_IMAGE";
    public static final String ARGS_EVENT_DATE = "ARGS_EVENT_DATE";
    public static final String ARGS_EVENT_THEME = "ARGS_EVENT_THEME";
    public static final String ARGS_EVENT_ADMIN = "ARGS_EVENT_ADMIN";
    public static final int EDIT_EVENT_REQUEST_CODE = 2000;

    /** Views **/
    @BindView(R.id.activity_event_detail_collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.activity_event_detail_appbar) AppBarLayout mAppBarLayout;
    @BindView(R.id.activity_event_detail_linearLayout) LinearLayout mLinearLayout;
    @BindView(R.id.activity_event_detail_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_event_detail_imageView) ImageView mBackground;
    @BindView(R.id.activity_event_detail_event_month) TextView mMonth;
    @BindView(R.id.activity_event_detail_event_day) TextView mDay;
    @BindView(R.id.activity_event_detail_theme) TextView mThemeView;
    @BindView(R.id.activity_event_detail_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.activity_event_detail_progressBar) ProgressBar mProgressBar;

    /** Instance **/
    @State protected int mEventID;
    @State protected String mEventName;
    @State protected String mGroupImage;
    @State protected String mTheme;
    @State protected LocalDateTime mDateTime;
    @State protected boolean mIsAdmin;
    private EventDetailsRecyclerAdapter mAdapter;
    private ArrayList<Object> mListItems = new ArrayList<>();
    private Event mEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        } else if (getIntent() != null) {
            mEventID = getIntent().getIntExtra(ARGS_EVENT_ID, -1);
            mEventName = getIntent().getStringExtra(ARGS_EVENT_NAME);
            mGroupImage = getIntent().getStringExtra(ARGS_GROUP_IMAGE);
            mDateTime = (LocalDateTime) getIntent().getSerializableExtra(ARGS_EVENT_DATE);
            mTheme = getIntent().getStringExtra(ARGS_EVENT_THEME);
            mIsAdmin = getIntent().getBooleanExtra(ARGS_EVENT_ADMIN, false);
        }

        setUpCollapsingLayout();

        mAdapter = new EventDetailsRecyclerAdapter(mListItems, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void setUpCollapsingLayout() {

        if (mGroupImage != null) {
            Picasso.with(this)
                    .load(mGroupImage.replace("upload/", "upload/e_brightness:-60,q_99/e_blur:1000/"))
                    .into(mBackground);
        }

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mLinearLayout.setAlpha(1 - (float)(0 - verticalOffset) / (2 *ViewCompat.getMinimumHeight(mCollapsingToolbar)));
            }
        });

        mCollapsingToolbar.setExpandedTitleColor(Color.WHITE); // transparent color = #00000000
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE); //Color of your title
        setUpEventSpecific();
    }

    private void setUpEventSpecific() {
        mCollapsingToolbar.setTitle(mEventName);
        mMonth.setText(mDateTime.toString(DateTimeFormat.forPattern("MMM")));
        mDay.setText(String.valueOf(mDateTime.getDayOfMonth()));
        mThemeView.setText(mTheme);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mIsAdmin) getMenuInflater().inflate(R.menu.menu_event_details_admin, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        EventRequest tokenRequest = new EventRequest(Util.getToken(this), mEventID);
        BusProvider.getInstance().post(new GetEventDetailEvent.OnLoadingStart(tokenRequest));
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.setContext(null);
        mRecyclerView.setAdapter(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_event_detail_cancel_event:
                cancelEvent();
                return true;
            case R.id.menu_event_detail_edit_event:
                editEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe
    public void onEventLoaded(GetEventDetailEvent.OnLoaded onLoaded) {
        Event event = onLoaded.getResponse().getEvent();
        ArrayList<Attendee> attendees = onLoaded.getResponse().getAttendees();

        if (attendees != null) {
            mListItems.clear();
            mListItems.addAll(attendees);
        }

        if (event != null) {
            mEvent = event;
            String time = event.getEventDateTime().toString(DateTimeFormat.forPattern("h:mmaa")).toLowerCase().replace(".", "");
            String date = event.getEventDateTime().toString(DateTimeFormat.forPattern("EEEE, MMMM d"));
            mAdapter.setDetails(event.getLocation(), time, event.getNotes(), date, event.getTheme());
        }

        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEventLoadFailed(GetMembersForGroupEvent.OnLoadingError onLoadingFailed) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, "onEventLoaded fail", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (mLinearLayout.getAlpha() < 0.1f) {finish();}
        else {supportFinishAfterTransition();}
    }

    private void cancelEvent() {
        CancelEventRequest request = new CancelEventRequest(Util.getToken(this), mEventID);
        BusProvider.getInstance().post(new CancelEventEvent.OnLoadingStart(request));
    }

    private void editEvent() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        intent.putExtra(CreateEventActivity.ARGS_EXISTING_EVENT, mEvent);
        startActivityForResult(intent, EDIT_EVENT_REQUEST_CODE);
    }

    @Subscribe
    public void onEventCancelled(CancelEventEvent.OnLoaded onLoaded) {
        Toast.makeText(this, mEventName + " cancelled", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_EVENT_REQUEST_CODE) {
                mEventName = data.getStringExtra(ARGS_EVENT_NAME);
                mDateTime = (LocalDateTime) data.getSerializableExtra(ARGS_EVENT_DATE);
                mTheme = data.getStringExtra(ARGS_EVENT_THEME);
                setUpEventSpecific();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}

//        mAttendingQuestion.setVisibility(View.INVISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Animation bottomUp = AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.bottom_up);
//                if (bottomUp != null) {
//                    mAttendingQuestion.startAnimation(bottomUp);
//                    mAttendingQuestion.setVisibility(View.VISIBLE);
//                }
//            }
//        }, 2000/* 1sec delay */);