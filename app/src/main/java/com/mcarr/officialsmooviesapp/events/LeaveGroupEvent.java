package com.mcarr.officialsmooviesapp.events;

import com.mcarr.officialsmooviesapp.object.request.LeaveGroupRequest;
import com.mcarr.officialsmooviesapp.object.response.BaseResponse;

/**********************************
 * Created by Mikel on 25-May-16.
 *********************************/
public class LeaveGroupEvent extends BaseNetworkEvent {

    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnLoaded extends OnDone<BaseResponse> {
        public OnLoaded(BaseResponse response) {
            super(response);
        }
    }

    public static class OnLoadingStart extends OnStart<LeaveGroupRequest> {
        public OnLoadingStart(LeaveGroupRequest tokenRequest) {
            super(tokenRequest);
        }
    }

    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }

}
