package com.mcarr.officialsmooviesapp.listadapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcarr.officialsmooviesapp.listitems.MyGroupListItemView;
import com.mcarr.officialsmooviesapp.object.GroupListItem;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 22-Jun-16.
 *********************************/
public class MyGroupsRecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<GroupListItem> mListItems;
    private OnMyGroupClickListener mListener;

    public MyGroupsRecyclerAdapter(ArrayList<GroupListItem> listItems) {
        mListItems = listItems;
    }

    public void setOnGroupClickListener(OnMyGroupClickListener listener) {
        mListener = listener;
    }

    public GroupListItem getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(MyGroupListItemView.getLayoutID(), parent, false);
        MyGroupListItemView.MyGroupListItemViewListener listener = new MyGroupListItemView.MyGroupListItemViewListener() {
            @Override
            public void onItemClick(GroupListItem groupListItem, ImageView imageView, TextView name) {
                if (mListener != null) mListener.onGroupClick(groupListItem, imageView, name);
            }
        };
        return new MyGroupListItemView (view, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyGroupListItemView viewHolder = ((MyGroupListItemView) holder);
        GroupListItem item = getItem(position);
        viewHolder.setView(item);
    }

    public interface OnMyGroupClickListener {
        void onGroupClick(GroupListItem groupListItem, ImageView imageView, TextView name);
    }
}