package com.mcarr.officialsmooviesapp.listitems;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mcarr.officialsmooviesapp.R;

/**********************************
 * Created by Mikel on 30-Jun-16.
 *********************************/
public class AttendeeTitleListItemView extends RecyclerView.ViewHolder {

    private TextView titleView;

    public static int getLayoutID() {
        return R.layout.list_item_attendee_title;
    }

    public AttendeeTitleListItemView(View v) {
        super(v);
        titleView = (TextView) v.findViewById(R.id.list_item_attendee_title_title);
    }

    public void setView(String title) {
        titleView.setText(title);
    }
}
