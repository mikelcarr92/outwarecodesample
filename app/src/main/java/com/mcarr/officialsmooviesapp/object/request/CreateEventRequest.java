package com.mcarr.officialsmooviesapp.object.request;

/**********************************
 * Created by Mikel on 24-May-16.
 *********************************/
public class CreateEventRequest extends BaseRequest {

    private String name;
    private int groupID;
    private String theme;
    private String location;
    private String notes;
    private String eventDate;

    public CreateEventRequest(String token, String name, int groupID, String theme, String location, String notes, String eventDate) {
        super(token);
        this.name = name;
        this.groupID = groupID;
        this.theme = theme;
        this.location = location;
        this.notes = notes;
        this.eventDate = eventDate;
    }

    public String getName() {
        return name;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getTheme() {
        return theme;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public String getEventDate() {
        return eventDate;
    }
}
