package com.mcarr.officialsmooviesapp.object.response;

import com.mcarr.officialsmooviesapp.object.Attendee;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 25-May-16.
 *********************************/
public class GetAttendeesResponse extends BaseResponse {

    private ArrayList<Attendee> attendees;

    public GetAttendeesResponse() {}

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

}
