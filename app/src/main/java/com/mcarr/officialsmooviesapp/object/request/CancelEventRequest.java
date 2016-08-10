package com.mcarr.officialsmooviesapp.object.request;

/**
 * Created by Mikel-XPS on 01-Aug-16.
 * All credit goes to he.
 */
public class CancelEventRequest extends BaseRequest {

    private int eventID;

    public CancelEventRequest(String token, int eventID) {
        super(token);
        this.eventID = eventID;
    }

    public int getEventID() {
        return eventID;
    }

}
