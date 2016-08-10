package com.mcarr.officialsmooviesapp.object.response;

import com.mcarr.officialsmooviesapp.object.Attendee;
import com.mcarr.officialsmooviesapp.object.Event;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 30-May-16.
 *********************************/
public class GetEventDetailResponse extends BaseResponse {

    private Event event;
    private ArrayList<Attendee> attendees;

    public GetEventDetailResponse() {}

    public Event getEvent() {
        return event;
    }

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }
}
