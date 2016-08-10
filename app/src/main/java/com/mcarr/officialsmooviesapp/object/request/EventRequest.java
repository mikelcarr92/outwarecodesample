package com.mcarr.officialsmooviesapp.object.request;

/**********************************
 * Created by Mikel on 21-May-16.
 *********************************/
public class EventRequest extends BaseRequest {

    private int eventID;

    public EventRequest() {}

    public EventRequest(String token, int eventID) {
        super(token);
        this.eventID = eventID;
    }

    public int getEventID(){
        return eventID;
    }
}
