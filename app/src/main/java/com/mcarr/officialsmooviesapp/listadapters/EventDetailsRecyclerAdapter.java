package com.mcarr.officialsmooviesapp.listadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.listitems.AttendeeListItemView;
import com.mcarr.officialsmooviesapp.listitems.AttendeeTitleListItemView;
import com.mcarr.officialsmooviesapp.listitems.EventDetailListItemView;
import com.mcarr.officialsmooviesapp.object.Attendee;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 30-Jun-16.
 *********************************/
public class EventDetailsRecyclerAdapter extends RecyclerView.Adapter {

    private static final int TYPE_NOTES = 0;
    private static final int TYPE_ATTENDEES_HEADER = 1;
    private static final int TYPE_ATTENDEE = 2;

    private ArrayList<Object> mListItems;
    private Context mContext;
    private String mNotes = "";
    private String mTime = "";
    private String mLocation = "";
    private String mDate = "";
    private String mTheme = "";

    public EventDetailsRecyclerAdapter(ArrayList<Object> listItems, Context context) {
        mListItems = listItems;
        mContext = context;
    }

    private Object getItem(int position) {
        return mListItems.get(position);
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setDetails(String location, String time, String notes, String date, String theme) {
        mNotes = notes;
        mTime = time;
        mLocation = location;
        mDate = date;
        mTheme = theme;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_NOTES:
                return new EventDetailListItemView(inflater.inflate(EventDetailListItemView.getLayoutID(), parent, false));

            case TYPE_ATTENDEES_HEADER:
                return new AttendeeTitleListItemView (inflater.inflate(AttendeeTitleListItemView.getLayoutID(), parent, false));

            case TYPE_ATTENDEE:
                return new AttendeeListItemView (inflater.inflate(AttendeeListItemView.getLayoutID(), parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_NOTES:
                ((EventDetailListItemView) holder).setView(mLocation, mTime, mNotes, mDate, mTheme);
                break;
            case TYPE_ATTENDEES_HEADER:
                String title = mContext.getString(R.string.list_item_attendees_header_title, getAttendeeCount());
                ((AttendeeTitleListItemView) holder).setView(title);
                break;
            case TYPE_ATTENDEE:
                Attendee attendee = (Attendee) getItem(position - TYPE_ATTENDEE);
                ((AttendeeListItemView) holder).setView(attendee);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mListItems.size() + TYPE_ATTENDEE;
    }

    private int getAttendeeCount() {
        return mListItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_NOTES;
            case 1:
                return TYPE_ATTENDEES_HEADER;
            default:
                return TYPE_ATTENDEE;
        }
    }
}
