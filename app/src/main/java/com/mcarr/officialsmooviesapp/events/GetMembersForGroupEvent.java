package com.mcarr.officialsmooviesapp.events;

import com.mcarr.officialsmooviesapp.object.request.GroupRequest;
import com.mcarr.officialsmooviesapp.object.response.GetGroupMembersResponse;

/**********************************
 * Created by Mikel on 25-May-16.
 *********************************/
public class GetMembersForGroupEvent extends BaseNetworkEvent {

    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnLoaded extends OnDone<GetGroupMembersResponse> {
        public OnLoaded(GetGroupMembersResponse response) {
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
