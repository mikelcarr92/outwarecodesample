package com.mcarr.officialsmooviesapp.object.response;

import com.mcarr.officialsmooviesapp.object.Member;

import java.util.ArrayList;

/**********************************
 * Created by Mikel on 25-May-16.
 *********************************/
public class GetGroupMembersResponse extends BaseResponse {

    private ArrayList<Member> members;

    public GetGroupMembersResponse() {}

    public ArrayList<Member> getMembers() {
        return members;
    }

}
