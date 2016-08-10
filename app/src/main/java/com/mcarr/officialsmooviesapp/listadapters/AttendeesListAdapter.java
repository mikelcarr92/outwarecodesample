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
import com.mcarr.officialsmooviesapp.object.Attendee;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class AttendeesListAdapter extends BaseAdapter {

    private ArrayList<Attendee> mListItems;
    private Context mContext;
    private String[] mStatusArray;

    public AttendeesListAdapter(Context context, ArrayList<Attendee> data) {
        mContext = context;
        mListItems = data;
        mStatusArray = mContext.getResources().getStringArray(R.array.attending_status);
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Attendee getItem(int position) {
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
            row = inflater.inflate(R.layout.list_item_attendee, parent, false);
            viewHolder = new GroupViewHolder();

            viewHolder.letterImageView = (ImageView) row.findViewById(R.id.list_item_attendee_imageView);
            viewHolder.circleImageView = (CircleImageView) row.findViewById(R.id.list_item_attendee_circleImageView);
            viewHolder.name = (TextView) row.findViewById(R.id.list_item_attendee_name);
            viewHolder.status = (TextView) row.findViewById(R.id.list_item_attendee_status);

            row.setTag(viewHolder);

        } else {
            viewHolder = (GroupViewHolder) row.getTag();
        }

        Attendee attendee = getItem(position);

        viewHolder.name.setText(String.valueOf(attendee.getName()));

        if (attendee.getProfileImage() == null || attendee.getProfileImage().length() == 0) {
            String firstLetter = attendee.getName().substring(0, 1);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(firstLetter, Util.calculateColorFromString(attendee.getName()));
            viewHolder.letterImageView.setImageDrawable(drawable);
            viewHolder.circleImageView.setVisibility(View.GONE);
        } else {
            Picasso.with(mContext).load(attendee.getProfileImage()).into(viewHolder.circleImageView);
            viewHolder.letterImageView.setVisibility(View.GONE);
        }

        String status = mStatusArray[attendee.getStatus()];
        viewHolder.status.setText(status);

        return row;
    }

    public class GroupViewHolder {
        public ImageView letterImageView;
        public CircleImageView circleImageView;
        public TextView name;
        public TextView status;

    }
}