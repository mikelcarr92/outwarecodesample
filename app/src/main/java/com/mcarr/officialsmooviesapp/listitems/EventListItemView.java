package com.mcarr.officialsmooviesapp.listitems;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.object.EventListItem;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**********************************
 * Created by Mikel on 22-Jun-16.
 *********************************/
public class EventListItemView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMM");

    private ColorGenerator generator = ColorGenerator.MATERIAL;

    private TextView name;
    private TextView details;
    private TextView location;
    private View calendar;
    private TextView month;
    private TextView day;
    private CardView cardView;
    private LinearLayout fade;
    private EventListItemViewListener mListener;

    private EventListItem mEventListItem;

    public EventListItemView(View v, EventListItemViewListener listener) {
        super(v);
        mListener = listener;
        name = (TextView) v.findViewById(R.id.list_item_event_name);
        details = (TextView) v.findViewById(R.id.list_item_event_details);
        location = (TextView) v.findViewById(R.id.list_item_event_location);
        calendar = v.findViewById(R.id.list_item_event_calendar);
        month = (TextView) v.findViewById(R.id.list_item_event_month);
        day = (TextView) v.findViewById(R.id.list_item_event_day);
        cardView = (CardView) v.findViewById(R.id.list_item_event_cardView);
        fade = (LinearLayout) v.findViewById(R.id.list_item_event_fade);
        cardView.setOnClickListener(this);
    }

    public static int getLayoutID() {
        return R.layout.list_item_event;
    }

    public void setView(EventListItem eventListItem) {
        mEventListItem = eventListItem;
        name.setText(String.valueOf(eventListItem.getName()));
        name.setTextColor(generator.getColor(eventListItem.getName()));
        details.setText(eventListItem.getGroupName());
        location.setText(eventListItem.getLocation());
        month.setText(eventListItem.getEventDateTime().toString(dateTimeFormatter));
        day.setText(String.valueOf(eventListItem.getEventDateTime().getDayOfMonth()));
        fade.setVisibility(eventListItem.getEventDateTime().isAfter(LocalDateTime.now()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(mEventListItem, calendar, month, day);
        }
    }

    public interface EventListItemViewListener {
        void onItemClick(EventListItem eventListItem, View calendarView, View month, View day);
    }
}