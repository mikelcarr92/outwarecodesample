package com.mcarr.officialsmooviesapp.object.request;

/**********************************
 * Created by Mikel on 25-May-16.
 *********************************/
public class LeaveGroupRequest extends BaseRequest {

    private int groupID;

    public LeaveGroupRequest(int groupID, String token) {
        super(token);
        this.groupID = groupID;
    }

    public int getGroupID() {
        return groupID;
    }

}
