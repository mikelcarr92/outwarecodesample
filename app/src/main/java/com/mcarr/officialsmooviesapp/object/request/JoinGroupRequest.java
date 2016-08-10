package com.mcarr.officialsmooviesapp.object.request;

/**********************************
 * Created by Mikel on 25-May-16.
 *********************************/
public class JoinGroupRequest extends BaseRequest {

    private int groupID;
    private String password;

    public JoinGroupRequest(int groupID, String token, String password) {
        super(token);
        this.groupID = groupID;
        this.password = password;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getPassword() {
        return password;
    }
}
