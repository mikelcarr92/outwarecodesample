package com.mcarr.officialsmooviesapp.object.request;

/**********************************
 * Created by Mikel on 21-May-16.
 *********************************/
public class GroupRequest extends BaseRequest {

    private int groupID;

    public GroupRequest() {}

    public GroupRequest(String token, int groupID) {
        super(token);
        this.groupID = groupID;
    }

    public int getGroupID(){
        return groupID;
    }
}
