package com.mcarr.officialsmooviesapp.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.object.GroupListItem;
import com.mcarr.officialsmooviesapp.util.Util;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class GroupsListAdapter extends BaseAdapter {

    private ArrayList<GroupListItem> mListItems;
    private Context mContext;
    private boolean mMyGroups;

    public GroupsListAdapter(Context context, ArrayList<GroupListItem> groups, boolean myGroups) {
        mContext = context;
        mListItems = groups;
        mMyGroups = myGroups;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public GroupListItem getItem(int position) {
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

            if (mMyGroups) {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_item_my_group, parent, false);
                viewHolder = new GroupViewHolder();

                viewHolder.name = (TextView) row.findViewById(R.id.list_item_my_groups_name);
                viewHolder.details = (TextView) row.findViewById(R.id.list_item_my_groups_details);
                viewHolder.imageView = (ImageView) row.findViewById(R.id.list_item_my_groups_imageView);

            } else {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_item_groups, parent, false);
                viewHolder = new GroupViewHolder();

                viewHolder.name = (TextView) row.findViewById(R.id.list_item_groups_name);
                viewHolder.details = (TextView) row.findViewById(R.id.list_item_groups_details);
                viewHolder.imageView = (ImageView) row.findViewById(R.id.list_item_groups_imageView);
            }

            row.setTag(viewHolder);

        } else {
            viewHolder = (GroupViewHolder) row.getTag();
        }

        GroupListItem currentGroup = getItem(position);

        viewHolder.name.setText(String.valueOf(currentGroup.getName()));
        viewHolder.details.setText(currentGroup.getMemberCount() + " members");

        String firstLetter = currentGroup.getName().substring(0, 1);
        //TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, ContextCompat.getColor(mContext, R.color.colorAccent));
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(firstLetter, Util.calculateColorFromString(currentGroup.getName()));
        viewHolder.imageView.setImageDrawable(drawable);

        return row;
    }

    public class GroupViewHolder {
        public TextView name;
        public TextView details;
        public ImageView imageView;

    }
}