package com.mcarr.officialsmooviesapp.events;

import com.mcarr.officialsmooviesapp.object.request.CreateEventRequest;
import com.mcarr.officialsmooviesapp.object.response.CreateEventResponse;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public class CreateEventEvent extends BaseNetworkEvent {

    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnLoaded extends OnDone<CreateEventResponse> {
        public OnLoaded(CreateEventResponse response) {
            super(response);
        }
    }

    public static class OnLoadingStart extends OnStart<CreateEventRequest> {
        public OnLoadingStart(CreateEventRequest tokenRequest) {
            super(tokenRequest);
        }
    }

    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }
}
