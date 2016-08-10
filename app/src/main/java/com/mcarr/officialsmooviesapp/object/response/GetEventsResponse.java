package com.mcarr.officialsmooviesapp.object.response;

import com.mcarr.officialsmooviesapp.object.EventListItem;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 30-May-16.
 *********************************/
public class GetEventsResponse extends BaseResponse {

    private ArrayList<EventListItem> events;

    public GetEventsResponse() {}

    public ArrayList<EventListItem> getEvents() {
        return events;
    }
}
