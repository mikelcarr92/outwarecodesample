package com.mcarr.officialsmooviesapp.object;

/**********************************
 * Created by Mikel on 25-May-16.
 *********************************/
public class Member {

    private int id;
    private String name;
    private String profileImage;
    private String joinDate;
    private boolean isAdmin;

    public Member() {}

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
