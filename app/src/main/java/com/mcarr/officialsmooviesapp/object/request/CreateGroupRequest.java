package com.mcarr.officialsmooviesapp.object.request;

/**********************************
 * Created by Mikel on 24-May-16.
 *********************************/
public class CreateGroupRequest extends BaseRequest {

    private String name;
    private String password;

    public CreateGroupRequest(String token, String name, String password) {
        super(token);
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
