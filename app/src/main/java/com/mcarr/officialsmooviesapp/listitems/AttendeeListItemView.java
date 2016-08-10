package com.mcarr.officialsmooviesapp.listitems;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.object.Attendee;
import com.mcarr.officialsmooviesapp.util.Util;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**********************************
 * Created by Mikel on 30-Jun-16.
 *********************************/
public class AttendeeListItemView extends RecyclerView.ViewHolder {

    private ImageView letterImageView;
    private CircleImageView circleImageView;
    private TextView name;
    private TextView status;

    public static int getLayoutID() {
        return R.layout.list_item_attendee;
    }

    public AttendeeListItemView(View v) {
        super(v);
        letterImageView = (ImageView) v.findViewById(R.id.list_item_attendee_imageView);
        circleImageView = (CircleImageView) v.findViewById(R.id.list_item_attendee_circleImageView);
        name = (TextView) v.findViewById(R.id.list_item_attendee_name);
        status = (TextView) v.findViewById(R.id.list_item_attendee_status);
    }

    public void setView(Attendee attendee) {

        name.setText(String.valueOf(attendee.getName()));

        if (attendee.getProfileImage() == null || attendee.getProfileImage().length() == 0) {

            String firstLetter = attendee.getName().substring(0, 1);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .bold()
                    .toUpperCase()
                    .endConfig()
                    .buildRound(firstLetter, Util.calculateColorFromString(attendee.getName()));
            letterImageView.setImageDrawable(drawable);
            circleImageView.setVisibility(View.GONE);

        } else {

            Picasso.with(name.getContext())
                    .load(attendee.getProfileImage())
                    .into(circleImageView);

            letterImageView.setVisibility(View.GONE);
        }
    }
}
