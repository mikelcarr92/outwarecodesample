package com.mcarr.officialsmooviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.GetEventsEvent;
import com.mcarr.officialsmooviesapp.events.ProfileImgUploadDoneEvent;
import com.mcarr.officialsmooviesapp.listadapters.EventsRecyclerAdapter;
import com.mcarr.officialsmooviesapp.listitems.EventListItemView;
import com.mcarr.officialsmooviesapp.object.EventListItem;
import com.mcarr.officialsmooviesapp.object.request.BaseRequest;
import com.mcarr.officialsmooviesapp.object.response.GetEventsResponse;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class MainActivity extends AppCompatActivity implements EventListItemView.EventListItemViewListener {

    /**
     * TODO
     * - Make edit event (update event) server side in CreateEventActivity if mEvent != null
     * - Set attending status in EventDetailActivity
     *
     */

    /** Views **/
    @BindView(R.id.activity_events_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.activity_events_progressBar) ProgressBar mProgressBar;

    /** Instance **/
    private ArrayList<EventListItem> mListItems = new ArrayList<>();
    private EventsRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);
        setTitle("Smoovies");

        mRecyclerAdapter = new EventsRecyclerAdapter(mListItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setOnEventClickListener(this);
    }

    private void logout() {

    }

    private void launchGroups() {
        Intent intent = new Intent(MainActivity.this, MyGroupsActivity.class);
        startActivity(intent);
    }

    private void launchSettings() {

    }

    private void launchMyProfile() {
        Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_main_logout:
                logout();
                return true;
            case R.id.menu_activity_main_groups:
                launchGroups();
                return true;
            case R.id.menu_activity_main_settings:
                launchSettings();
                return true;
            case R.id.menu_activity_main_my_profile:
                launchMyProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
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
        BaseRequest tokenRequest = new BaseRequest(Util.getToken(this));
        BusProvider.getInstance().post(new GetEventsEvent.OnLoadingStart(tokenRequest));
    }

    @Subscribe
    public void onGroupsLoaded(GetEventsEvent.OnLoaded onLoaded) {
        GetEventsResponse response = onLoaded.getResponse();
        mProgressBar.setVisibility(View.GONE);
        mListItems.clear();
        mListItems.addAll(response.getEvents());
        mRecyclerAdapter.setUpHeaders();
        mRecyclerAdapter.notifyDataSetUpdated();
    }

    @Subscribe
    public void onGroupsFailed(GetEventsEvent.OnLoadingError onLoadingFailed) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, "load failed", Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void totesmalotes(ProfileImgUploadDoneEvent event) {
        Toast.makeText(this, "TOTES MALOTES", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(EventListItem eventListItem, View calendarView, View month, View day) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.ARGS_EVENT_ID, eventListItem.getId());
        intent.putExtra(EventDetailActivity.ARGS_EVENT_NAME, eventListItem.getName());
        intent.putExtra(EventDetailActivity.ARGS_GROUP_IMAGE, eventListItem.getGroupImage());
        intent.putExtra(EventDetailActivity.ARGS_EVENT_DATE, eventListItem.getEventDateTime());
        intent.putExtra(EventDetailActivity.ARGS_EVENT_THEME, eventListItem.getTheme());
        intent.putExtra(EventDetailActivity.ARGS_EVENT_ADMIN, eventListItem.isAdmin());
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, calendarView, "calendarView");
        Pair<View, String> p2 = Pair.create(month, "calendarViewMonth");
        Pair<View, String> p1 = Pair.create(day, "calendarViewDay");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);
        startActivity(intent, options.toBundle());
    }
}