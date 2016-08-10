package com.mcarr.officialsmooviesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.mcarr.officialsmooviesapp.events.GetEventsEvent;
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
 * Created by Mikel on 30-May-16.
 *********************************/
public class EventListFragment extends ParentFragment implements EventListItemView.EventListItemViewListener {

    /** Views **/
    @BindView(R.id.activity_events_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.activity_events_progressBar) ProgressBar mProgressBar;

    /** Instance **/
    private ArrayList<EventListItem> mListItems = new ArrayList<>();
    //private ColorGenerator generator = ColorGenerator.MATERIAL;
    private EventsRecyclerAdapter mRecyclerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((DaggerApplication) getActivity().getApplication()).getNetComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.activity_events, container, false);
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
        BaseRequest tokenRequest = new BaseRequest(Util.getToken(getActivity()));
        BusProvider.getInstance().post(new GetEventsEvent.OnLoadingStart(tokenRequest));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mRecyclerAdapter = new EventsRecyclerAdapter(mListItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setOnEventClickListener(this);
    }

    @Subscribe
    public void onGroupsLoaded(GetEventsEvent.OnLoaded onLoaded) {
        GetEventsResponse response = onLoaded.getResponse();
        mProgressBar.setVisibility(View.GONE);
        mListItems.clear();
        mListItems.addAll(response.getEvents());
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onGroupsFailed(GetEventsEvent.OnLoadingError onLoadingFailed) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "load failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(EventListItem eventListItem, View calendarView, View month, View day) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.ARGS_EVENT_ID, eventListItem.getId());
        intent.putExtra(EventDetailActivity.ARGS_EVENT_NAME, eventListItem.getName());
        intent.putExtra(EventDetailActivity.ARGS_GROUP_IMAGE, eventListItem.getGroupImage());
        intent.putExtra(EventDetailActivity.ARGS_EVENT_DATE, eventListItem.getEventDateTime());
        intent.putExtra(EventDetailActivity.ARGS_EVENT_THEME, eventListItem.getTheme());
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), calendarView, "calendarView");

        Pair<View, String> p1 = Pair.create(month, "calendarViewMonth");
        Pair<View, String> p2 = Pair.create(day, "calendarViewDay");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2);
        getActivity().startActivity(intent, options.toBundle());
    }
}
