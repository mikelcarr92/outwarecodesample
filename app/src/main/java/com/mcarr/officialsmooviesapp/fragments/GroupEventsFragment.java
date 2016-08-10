package com.mcarr.officialsmooviesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.activities.EventDetailActivity;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.GetEventsForGroupEvent;
import com.mcarr.officialsmooviesapp.listadapters.EventsRecyclerAdapter;
import com.mcarr.officialsmooviesapp.listitems.EventListItemView;
import com.mcarr.officialsmooviesapp.object.EventListItem;
import com.mcarr.officialsmooviesapp.object.request.GroupRequest;
import com.mcarr.officialsmooviesapp.object.response.GetEventsResponse;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**********************************
 * Created by Mikel on 07-Jun-16.
 *********************************/
public class GroupEventsFragment extends ParentFragment {

    /** Static **/
    private static final String ARGS_GROUP_ID = "ARGS_GROUP_ID";

    /** Views **/
    @BindView(R.id.fragment_group_events_progressBar) ProgressBar mProgressBarView;
    @BindView(R.id.fragment_group_events_recyclerView) RecyclerView mRecyclerView;

    /** Instance **/
    private EventsRecyclerAdapter mRecyclerAdapter;
    private ArrayList<EventListItem> mListItems = new ArrayList<>();
    private int mGroupID;

    public static GroupEventsFragment newInstance(int groupID) {
        GroupEventsFragment fragment = new GroupEventsFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_GROUP_ID, groupID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_group_events, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (getArguments() != null) {
            mGroupID = getArguments().getInt(ARGS_GROUP_ID, -1);
        }

        mRecyclerAdapter = new EventsRecyclerAdapter(mListItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setOnEventClickListener(new EventListItemView.EventListItemViewListener() {
            @Override
            public void onItemClick(EventListItem eventListItem, View calendarView, View month, View day) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra(EventDetailActivity.ARGS_EVENT_ID, eventListItem.getId());
                intent.putExtra(EventDetailActivity.ARGS_EVENT_NAME, eventListItem.getName());
                intent.putExtra(EventDetailActivity.ARGS_GROUP_IMAGE, eventListItem.getGroupImage());
                intent.putExtra(EventDetailActivity.ARGS_EVENT_DATE, eventListItem.getEventDateTime());
                intent.putExtra(EventDetailActivity.ARGS_EVENT_THEME, eventListItem.getTheme());
                Pair<View, String> p2 = Pair.create(month, "calendarViewMonth");
                Pair<View, String> p1 = Pair.create(day, "calendarViewDay");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);
                startActivity(intent, options.toBundle());
            }
        });
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
        BusProvider.getInstance().post(new GetEventsForGroupEvent.OnLoadingStart(tokenRequest));
    }

    @Subscribe
    public void onEventsLoaded(GetEventsForGroupEvent.OnLoaded onLoaded) {
        GetEventsResponse response = onLoaded.getResponse();
        mProgressBarView.setVisibility(View.GONE);
        mListItems.clear();
        mListItems.addAll(response.getEvents());
        mRecyclerAdapter.setUpHeaders();
        mRecyclerAdapter.notifyDataSetUpdated();
    }

    @Subscribe
    public void onEventsFailed(GetEventsForGroupEvent.OnLoadingError onLoadingFailed) {
        mProgressBarView.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "load failed", Toast.LENGTH_LONG).show();
    }
}