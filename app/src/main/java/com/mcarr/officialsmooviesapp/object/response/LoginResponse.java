package com.mcarr.officialsmooviesapp.object.response;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public class LoginResponse extends BaseResponse {

    private String token;
    private String name;
    private String profileImage;

    public LoginResponse() {}

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

}
