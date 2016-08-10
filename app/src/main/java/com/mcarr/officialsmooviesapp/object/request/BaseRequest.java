package com.mcarr.officialsmooviesapp.object.request;

/**********************************
 * Created by Mikel on 21-May-16.
 *********************************/
public class BaseRequest {

    private String token;

    public BaseRequest() {}

    public BaseRequest(String token) {
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}
