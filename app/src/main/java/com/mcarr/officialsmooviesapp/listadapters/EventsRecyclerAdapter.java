package com.mcarr.officialsmooviesapp.listadapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcarr.officialsmooviesapp.listitems.EventListItemView;
import com.mcarr.officialsmooviesapp.listitems.TitleListItemView;
import com.mcarr.officialsmooviesapp.object.EventListItem;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 22-Jun-16.
 *********************************/
public class EventsRecyclerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_EVENT = 1;

    private ArrayList<Object> mListItems = new ArrayList<>();
    private ArrayList<EventListItem> mEventListItems;
    private EventListItemView.EventListItemViewListener mListener;

    public EventsRecyclerAdapter(ArrayList<EventListItem> listItems) {
        mEventListItems = listItems;
    }

    public void notifyDataSetUpdated() {
        setUpHeaders();
        super.notifyDataSetChanged();
    }

    public void setUpHeaders() {

        mListItems.clear();
        if (mEventListItems.size() == 0) { return; }

        if (mEventListItems.get(0).getEventDateTime().isAfter(LocalDateTime.now())) {
            String futureEvents = "Upcoming Events";
            mListItems.add(futureEvents);
        }

        boolean addedPreviousEvents = false;

        for (EventListItem eventListItem : mEventListItems) {
            if (eventListItem.getEventDateTime().isBefore(LocalDateTime.now()) && !addedPreviousEvents) {
                addedPreviousEvents = true;
                String previousEvents = "Previous Events";
                mListItems.add(previousEvents);
            }
            mListItems.add(eventListItem);
        }
    }

    public void setOnEventClickListener(EventListItemView.EventListItemViewListener listener) {
        mListener = listener;
    }

    public Object getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof String) {
            return TYPE_HEADER;
        } else if (getItem(position) instanceof EventListItem) {
            return TYPE_EVENT;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case TYPE_EVENT:

                view = inflater.inflate(EventListItemView.getLayoutID(), parent, false);
                return new EventListItemView (view, mListener);

            case TYPE_HEADER:

                view = inflater.inflate(TitleListItemView.getLayoutID(), parent, false);
                return new TitleListItemView (view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case TYPE_EVENT:
                EventListItemView viewHolder = ((EventListItemView) holder);
                EventListItem item = (EventListItem) getItem(position);
                viewHolder.setView(item);
                break;

            case TYPE_HEADER:
                ((TitleListItemView) holder).setView((String) getItem(position));
                break;
        }
    }
}