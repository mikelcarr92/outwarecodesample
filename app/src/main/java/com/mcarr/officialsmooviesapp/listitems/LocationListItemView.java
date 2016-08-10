package com.mcarr.officialsmooviesapp.listitems;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mcarr.officialsmooviesapp.R;

/**********************************
 * Created by Mikel on 30-Jun-16.
 *********************************/
public class LocationListItemView extends RecyclerView.ViewHolder {

    private TextView locationView;

    public static int getLayoutID() {
        return R.layout.list_item_location;
    }

    public LocationListItemView(View v) {
        super(v);
        locationView = (TextView) v.findViewById(R.id.list_item_location_text);
    }

    public void setView(String location) {
        locationView.setText(location);
    }
}
