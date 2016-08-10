package com.mcarr.officialsmooviesapp.events;

import com.mcarr.officialsmooviesapp.object.request.CreateGroupRequest;
import com.mcarr.officialsmooviesapp.object.response.CreateGroupResponse;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public class CreateGroupEvent extends BaseNetworkEvent {

    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnLoaded extends OnDone<CreateGroupResponse> {
        public OnLoaded(CreateGroupResponse response) {
            super(response);
        }
    }

    public static class OnLoadingStart extends OnStart<CreateGroupRequest> {
        public OnLoadingStart(CreateGroupRequest tokenRequest) {
            super(tokenRequest);
        }
    }

    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }

}
