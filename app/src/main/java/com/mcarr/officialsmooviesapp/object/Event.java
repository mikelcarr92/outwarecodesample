package com.mcarr.officialsmooviesapp.object;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;

/**********************************
 * Created by Mikel on 06-Jun-16.
 *********************************/
public class Event implements Serializable {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private int id;
    private String name;
    private int groupID;
    private String theme;
    private String location;
    private String notes;
    private String eventDate;
    private String groupName;
    private String groupImage;
    private boolean isAdmin;

    public Event() {}

    public int getId() {
        return id;
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

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getEventDateTime() {
        return LocalDateTime.parse(eventDate, DateTimeFormat.forPattern(DATE_PATTERN));
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getLocation() {
        return location;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
