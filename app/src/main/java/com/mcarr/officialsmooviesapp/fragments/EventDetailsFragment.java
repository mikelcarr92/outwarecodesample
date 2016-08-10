package com.mcarr.officialsmooviesapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.events.GetEventDetailEvent;
import com.mcarr.officialsmooviesapp.events.GetMembersForGroupEvent;
import com.mcarr.officialsmooviesapp.object.Event;
import com.mcarr.officialsmooviesapp.object.request.EventRequest;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**********************************
 * Created by Mikel on 06-Jun-16.
 *********************************/
public class EventDetailsFragment extends ParentFragment {

    /** Static **/
    private static final String ARGS_EVENT_ID = "ARGS_EVENT_ID";

    /** Views **/
    @BindView(R.id.fragment_event_details_progressBar) ProgressBar mProgressBar;
//    @BindView(R.id.fragment_event_details_content) LinearLayout mContent;
//    @BindView(R.id.fragment_event_details_date) TextView mDate;
//    @BindView(R.id.fragment_event_details_theme) TextView mTheme;
//    @BindView(R.id.fragment_event_details_notes) TextView mNotes;

    /** Instance **/
    private int mEventID;

    public static EventDetailsFragment newInstance(int eventID) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_EVENT_ID, eventID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, mRootView);
        if (getArguments() != null) {
            mEventID = getArguments().getInt(ARGS_EVENT_ID, -1);
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        EventRequest tokenRequest = new EventRequest(Util.getToken(getActivity()), mEventID);
        BusProvider.getInstance().post(new GetEventDetailEvent.OnLoadingStart(tokenRequest));
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onEventLoaded(GetEventDetailEvent.OnLoaded onLoaded) {
        Event event = onLoaded.getResponse().getEvent();
        if (event != null) {
//            mDate.setText(event.getEventDate());
//            mTheme.setText('"' + event.getTheme() + '"');
//            mNotes.setText(event.getNotes());
//            mContent.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEventLoadFailed(GetMembersForGroupEvent.OnLoadingError onLoadingFailed) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "onEventLoaded fail", Toast.LENGTH_LONG).show();
    }
}
