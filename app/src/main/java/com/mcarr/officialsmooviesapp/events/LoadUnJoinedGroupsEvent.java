package com.mcarr.officialsmooviesapp.events;

import com.mcarr.officialsmooviesapp.object.response.GroupsResponse;
import com.mcarr.officialsmooviesapp.object.request.BaseRequest;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public class LoadUnJoinedGroupsEvent extends BaseNetworkEvent {

    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnLoaded extends OnDone<GroupsResponse> {
        public OnLoaded(GroupsResponse response) {
            super(response);
        }
    }

    public static class OnLoadingStart extends OnStart<BaseRequest> {
        public OnLoadingStart(BaseRequest request) {
            super(request);
        }
    }

    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }

}
