package com.mcarr.officialsmooviesapp.object.response;

import com.mcarr.officialsmooviesapp.object.Group;

/**********************************
 * Created by Mikel on 25-May-16.
 *********************************/
public class GetGroupDetailsResponse extends BaseResponse {

    private Group group;

    public GetGroupDetailsResponse() {}

    public Group getGroup() {
        return group;
    }

}
