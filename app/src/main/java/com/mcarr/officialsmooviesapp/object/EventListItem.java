package com.mcarr.officialsmooviesapp.object;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

/**********************************
 * Created by Mikel on 30-May-16.
 *********************************/
public class EventListItem {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private int id;
    private String name;
    private String location;
    private String eventDate;
    private String groupName;
    private int attendeeCount;
    private String groupImage;
    private String theme;
    private boolean isAdmin;

    public EventListItem() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEventDate() {
        return eventDate;
    }

    public LocalDateTime getEventDateTime() {
        return LocalDateTime.parse(eventDate, DateTimeFormat.forPattern(DATE_PATTERN));
    }

    public String getGroupName() {
        return groupName;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public String getLocation() {
        return location;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public String getTheme() {
        return theme;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
