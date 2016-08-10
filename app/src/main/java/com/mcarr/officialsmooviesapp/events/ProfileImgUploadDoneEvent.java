package com.mcarr.officialsmooviesapp.events;

import java.util.Map;

/**********************************
 * Created by Mikel on 23-Jun-16.
 *********************************/
public class ProfileImgUploadDoneEvent {

    private Map data;

    public ProfileImgUploadDoneEvent(Map data) {
        this.data = data;
    }

    public Map getData() {
        return data;
    }

}
