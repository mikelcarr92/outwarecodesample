package com.mcarr.officialsmooviesapp.listitems;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mcarr.officialsmooviesapp.R;

/**********************************
 * Created by Mikel on 30-Jun-16.
 *********************************/
public class EventDetailListItemView extends RecyclerView.ViewHolder {

    private TextView locationView;
    private TextView timeView;
    private TextView notesView;
    private TextView dateView;
    private TextView themeView;

    public static int getLayoutID() {
        return R.layout.list_item_event_details;
    }

    public EventDetailListItemView(View v) {
        super(v);
        locationView = (TextView) v.findViewById(R.id.list_item_event_details_location);
        timeView = (TextView) v.findViewById(R.id.list_item_event_details_time);
        notesView = (TextView) v.findViewById(R.id.list_item_event_details_notes);
        dateView = (TextView) v.findViewById(R.id.list_item_event_details_date);
        themeView = (TextView) v.findViewById(R.id.list_item_event_details_theme);
    }

    public void setView(String location, String time, String notes, String date, String theme) {
        locationView.setText(location);
        timeView.setText(time);
        notesView.setText(notes);
        dateView.setText(date);
        themeView.setText(theme);
    }
}
