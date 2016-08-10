package com.mcarr.officialsmooviesapp.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.object.Member;
import com.mcarr.officialsmooviesapp.util.Util;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class MembersListAdapter extends BaseAdapter {

    private ArrayList<Member> mListItems;
    private Context mContext;

    public MembersListAdapter(Context context, ArrayList<Member> members) {
        mContext = context;
        mListItems = members;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Member getItem(int position) {
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
            row = inflater.inflate(R.layout.list_item_member, parent, false);
            viewHolder = new GroupViewHolder();

            viewHolder.name = (TextView) row.findViewById(R.id.list_item_member_name);
            viewHolder.imageView = (ImageView) row.findViewById(R.id.list_item_member_imageView);
            viewHolder.admin = (TextView) row.findViewById(R.id.list_item_member_admin);

            row.setTag(viewHolder);

        } else {
            viewHolder = (GroupViewHolder) row.getTag();
        }

        Member member = getItem(position);

        viewHolder.name.setText(String.valueOf(member.getName()));

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(member.getName());

        String firstLetter = member.getName().substring(0, 1);

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .bold()
                .withBorder(Util.dpToPx(2, mContext))
                .toUpperCase()
                .endConfig()
                .buildRound(firstLetter, color);

        viewHolder.imageView.setImageDrawable(drawable);

        viewHolder.admin.setVisibility(member.isAdmin() ? View.VISIBLE : View.GONE);

        return row;
    }

    public class GroupViewHolder {
        public TextView name;
        public ImageView imageView;
        public TextView admin;
    }
}