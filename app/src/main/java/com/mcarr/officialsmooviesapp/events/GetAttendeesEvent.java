package com.mcarr.officialsmooviesapp.events;

import com.mcarr.officialsmooviesapp.object.request.EventRequest;
import com.mcarr.officialsmooviesapp.object.response.GetAttendeesResponse;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public class GetAttendeesEvent extends BaseNetworkEvent {

    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnLoaded extends OnDone<GetAttendeesResponse> {
        public OnLoaded(GetAttendeesResponse response) {
            super(response);
        }
    }

    public static class OnLoadingStart extends OnStart<EventRequest> {
        public OnLoadingStart(EventRequest tokenRequest) {
            super(tokenRequest);
        }
    }

    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }

}
