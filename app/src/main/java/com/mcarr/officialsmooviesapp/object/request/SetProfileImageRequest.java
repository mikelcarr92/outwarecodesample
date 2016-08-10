package com.mcarr.officialsmooviesapp.object.request;

/**********************************
 * Created by Mikel on 21-May-16.
 *********************************/
public class SetProfileImageRequest extends BaseRequest {

    private String url;

    public SetProfileImageRequest() {}

    public SetProfileImageRequest(String token, String url) {
        super(token);
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
}
