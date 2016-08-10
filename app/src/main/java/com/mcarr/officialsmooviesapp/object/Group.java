package com.mcarr.officialsmooviesapp.object;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class Group {

    private int id;
    private String name;
    private String dateCreated;
    private String createdBy;
    private boolean isAdmin;
    private String groupImage;

    public Group() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMemberCount() {
        return createdBy;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getGroupImage() {
        return groupImage;
    }
}
