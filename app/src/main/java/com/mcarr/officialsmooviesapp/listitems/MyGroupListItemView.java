package com.mcarr.officialsmooviesapp.listitems;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.object.GroupListItem;
import com.squareup.picasso.Picasso;

/**********************************
 * Created by Mikel on 22-Jun-16.
 *********************************/
public class MyGroupListItemView extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView name;
    private TextView details;
    private ImageView imageView;
    private CardView cardView;
    private MyGroupListItemViewListener mListener;

    private GroupListItem mGroupListItem;

    public MyGroupListItemView(View v, MyGroupListItemViewListener listener) {
        super(v);
        mListener = listener;
        name = (TextView) v.findViewById(R.id.list_item_my_groups_name);
        details = (TextView) v.findViewById(R.id.list_item_my_groups_details);
        imageView = (ImageView) v.findViewById(R.id.list_item_my_groups_imageView);
        cardView = (CardView) v.findViewById(R.id.list_item_my_groups_cardView);
        cardView.setOnClickListener(this);
    }

    public static int getLayoutID() {
        return R.layout.list_item_my_group;
    }

    public void setView(GroupListItem groupListItem) {
        mGroupListItem = groupListItem;
        name.setText(String.valueOf(groupListItem.getName()));
        details.setText(details.getContext().getString(R.string.my_group_list_item_members, groupListItem.getMemberCount()));
        Picasso.with(
                details.getContext())
                .load(groupListItem.getGroupImage())
                .into(imageView);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(mGroupListItem, imageView, name);
        }
    }

    public interface MyGroupListItemViewListener {
        void onItemClick(GroupListItem groupListItem, ImageView image, TextView title);
    }
}