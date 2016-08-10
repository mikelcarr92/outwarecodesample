package com.mcarr.officialsmooviesapp.events;

import com.mcarr.officialsmooviesapp.object.request.GroupRequest;
import com.mcarr.officialsmooviesapp.object.response.BaseResponse;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public class DisbandGroupEvent extends BaseNetworkEvent {

    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnLoaded extends OnDone<BaseResponse> {
        public OnLoaded(BaseResponse response) {
            super(response);
        }
    }

    public static class OnLoadingStart extends OnStart<GroupRequest> {
        public OnLoadingStart(GroupRequest request) {
            super(request);
        }
    }

    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }

}
