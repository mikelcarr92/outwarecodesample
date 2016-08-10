package com.mcarr.officialsmooviesapp.object.response;

import com.mcarr.officialsmooviesapp.object.GroupListItem;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class GroupsResponse extends BaseResponse {

    private ArrayList<GroupListItem> groups;

    public GroupsResponse() {}

    public ArrayList<GroupListItem> getGroups() {
        return groups;
    }

}
