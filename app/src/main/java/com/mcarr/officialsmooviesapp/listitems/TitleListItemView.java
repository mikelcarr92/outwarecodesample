package com.mcarr.officialsmooviesapp.listitems;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mcarr.officialsmooviesapp.R;

/**********************************
 * Created by Mikel on 22-Jun-16.
 *********************************/
public class TitleListItemView extends RecyclerView.ViewHolder {

    private TextView name;

    public TitleListItemView(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.list_item_title_text);
    }

    public static int getLayoutID() {
        return R.layout.list_item_title;
    }

    public void setView(String title) {
        name.setText(title);
    }
}