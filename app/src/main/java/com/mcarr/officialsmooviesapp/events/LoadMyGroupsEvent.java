package com.mcarr.officialsmooviesapp.events;

import com.mcarr.officialsmooviesapp.object.request.BaseRequest;
import com.mcarr.officialsmooviesapp.object.response.GroupsResponse;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public class LoadMyGroupsEvent extends BaseNetworkEvent {

    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnLoaded extends OnDone<GroupsResponse> {
        public OnLoaded(GroupsResponse response) {
            super(response);
        }
    }

    public static class OnLoadingStart extends OnStart<BaseRequest> {
        public OnLoadingStart(BaseRequest tokenRequest) {
            super(tokenRequest);
        }
    }

    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }

}
