package com.mcarr.officialsmooviesapp.object;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class GroupListItem {

    private int id;
    private String name;
    private String dateCreated;
    private int memberCount;
    private String groupImage;

    public GroupListItem() {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getGroupImage() {
        return groupImage;
    }
}
