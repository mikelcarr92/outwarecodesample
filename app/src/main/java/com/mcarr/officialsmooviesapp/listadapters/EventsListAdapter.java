package com.mcarr.officialsmooviesapp.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.object.EventListItem;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class EventsListAdapter extends BaseAdapter {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMM");

    private ArrayList<EventListItem> mListItems;
    private Context mContext;
    private ColorGenerator generator = ColorGenerator.MATERIAL;

    public EventsListAdapter(Context context, ArrayList<EventListItem> groups) {
        mContext = context;
        mListItems = groups;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public EventListItem getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        final GroupViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_event, parent, false);
            viewHolder = new GroupViewHolder();

            viewHolder.name = (TextView) row.findViewById(R.id.list_item_event_name);
            viewHolder.details = (TextView) row.findViewById(R.id.list_item_event_details);
            viewHolder.location = (TextView) row.findViewById(R.id.list_item_event_location);
            viewHolder.month = (TextView) row.findViewById(R.id.list_item_event_month);
            viewHolder.day = (TextView) row.findViewById(R.id.list_item_event_day);

            row.setTag(viewHolder);

        } else {
            viewHolder = (GroupViewHolder) row.getTag();
        }

        EventListItem eventListItem = getItem(position);

        viewHolder.name.setText(String.valueOf(eventListItem.getName()));
        viewHolder.name.setTextColor(generator.getColor(eventListItem.getName()));
        viewHolder.details.setText(eventListItem.getGroupName());
        viewHolder.location.setText(eventListItem.getLocation());

        LocalDateTime localDateTime = LocalDateTime.parse(eventListItem.getEventDate(), DateTimeFormat.forPattern(EventListItem.DATE_PATTERN));

        viewHolder.month.setText(localDateTime.toString(dateTimeFormatter));
        viewHolder.day.setText(String.valueOf(localDateTime.getDayOfMonth()));

        return row;
    }

    public class GroupViewHolder {
        public TextView name;
        public TextView details;
        public TextView location;
        public TextView month;
        public TextView day;
    }
}