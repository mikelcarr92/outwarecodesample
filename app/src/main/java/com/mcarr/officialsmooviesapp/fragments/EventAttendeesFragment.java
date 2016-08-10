package com.mcarr.officialsmooviesapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.GetAttendeesEvent;
import com.mcarr.officialsmooviesapp.listadapters.AttendeesListAdapter;
import com.mcarr.officialsmooviesapp.object.Attendee;
import com.mcarr.officialsmooviesapp.object.request.EventRequest;
import com.mcarr.officialsmooviesapp.object.response.GetAttendeesResponse;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**********************************
 * Created by Mikel on 06-Jun-16.
 *********************************/
public class EventAttendeesFragment extends ParentFragment {

    /** Static **/
    private static final String ARGS_EVENT_ID = "ARGS_EVENT_ID";

    /** Views **/
    @BindView(R.id.fragment_event_attendees_progressBar) ProgressBar mProgressBar;
    @BindView(R.id.fragment_event_attendees_listView) ListView mListView;

    /** Instance **/
    private int mEventID;
    private AttendeesListAdapter mAdapter;
    private ArrayList<Attendee> mListItems = new ArrayList<>();

    public static EventAttendeesFragment newInstance(int eventID) {
        EventAttendeesFragment fragment = new EventAttendeesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_EVENT_ID, eventID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_event_attendees, container, false);
        ButterKnife.bind(this, mRootView);
        if (getArguments() != null) {
            mEventID = getArguments().getInt(ARGS_EVENT_ID, -1);
        }
        return mRootView;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mAdapter = new AttendeesListAdapter(getActivity(), mListItems);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        EventRequest request = new EventRequest(Util.getToken(getActivity()), mEventID);
        BusProvider.getInstance().post(new GetAttendeesEvent.OnLoadingStart(request));
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onAttendeesLoaded(GetAttendeesEvent.OnLoaded onLoaded) {
        Log.i("DEV9", "onAttendeesLoaded");
        GetAttendeesResponse response = onLoaded.getResponse();
        mListItems.clear();
        mListItems.addAll(response.getAttendees());
        mAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
    }

    @Subscribe
    public void onAttendeesLoadFailed(GetAttendeesEvent.OnLoadingError onLoadingFailed) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "onAttendeesLoaded fail", Toast.LENGTH_LONG).show();
    }

}
