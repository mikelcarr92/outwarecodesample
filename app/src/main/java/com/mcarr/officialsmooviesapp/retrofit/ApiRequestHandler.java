package com.mcarr.officialsmooviesapp.retrofit;

import com.mcarr.officialsmooviesapp.events.CancelEventEvent;
import com.mcarr.officialsmooviesapp.events.CreateEventEvent;
import com.mcarr.officialsmooviesapp.events.CreateGroupEvent;
import com.mcarr.officialsmooviesapp.events.DisbandGroupEvent;
import com.mcarr.officialsmooviesapp.events.GetAttendeesEvent;
import com.mcarr.officialsmooviesapp.events.GetEventDetailEvent;
import com.mcarr.officialsmooviesapp.events.GetEventsEvent;
import com.mcarr.officialsmooviesapp.events.GetEventsForGroupEvent;
import com.mcarr.officialsmooviesapp.events.GetGroupDetailsEvent;
import com.mcarr.officialsmooviesapp.events.GetMembersForGroupEvent;
import com.mcarr.officialsmooviesapp.events.JoinGroupEvent;
import com.mcarr.officialsmooviesapp.events.LeaveGroupEvent;
import com.mcarr.officialsmooviesapp.events.LoadMyGroupsEvent;
import com.mcarr.officialsmooviesapp.events.LoadUnJoinedGroupsEvent;
import com.mcarr.officialsmooviesapp.object.response.BaseResponse;
import com.mcarr.officialsmooviesapp.object.response.CreateEventResponse;
import com.mcarr.officialsmooviesapp.object.response.CreateGroupResponse;
import com.mcarr.officialsmooviesapp.object.response.GetAttendeesResponse;
import com.mcarr.officialsmooviesapp.object.response.GetEventDetailResponse;
import com.mcarr.officialsmooviesapp.object.response.GetEventsResponse;
import com.mcarr.officialsmooviesapp.object.response.GetGroupDetailsResponse;
import com.mcarr.officialsmooviesapp.object.response.GetGroupMembersResponse;
import com.mcarr.officialsmooviesapp.object.response.GroupsResponse;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public class ApiRequestHandler {

    private Bus mBus;
    private ApiClient mApiClient;

    public ApiRequestHandler(Bus bus) {
        mBus = bus;
        mApiClient = ApiClient.getClient();
    }

    @Subscribe
    public void loadMyGroups (LoadMyGroupsEvent.OnLoadingStart onLoadingStart){

        mApiClient.getSmooviesApiService().getMyGroups(onLoadingStart.getRequest().getToken()).enqueue(new Callback<GroupsResponse>() {
            @Override
            public void onResponse(Call<GroupsResponse> call, Response<GroupsResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new LoadMyGroupsEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new LoadMyGroupsEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<GroupsResponse> call, Throwable t) {
                mBus.post(new LoadMyGroupsEvent.OnLoadingError("error!", -1));

            }
        });
    }

    @Subscribe
    public void loadUnjoinedGroups (LoadUnJoinedGroupsEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().getUnjoinedGroups(onLoadingStart.getRequest().getToken()).enqueue(new Callback<GroupsResponse>() {
            @Override
            public void onResponse(Call<GroupsResponse> call, Response<GroupsResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new LoadUnJoinedGroupsEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new LoadUnJoinedGroupsEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<GroupsResponse> call, Throwable t) {
                mBus.post(new LoadUnJoinedGroupsEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void createGroup (CreateGroupEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().createGroup(onLoadingStart.getRequest()).enqueue(new Callback<CreateGroupResponse>() {
            @Override
            public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new CreateGroupEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new CreateGroupEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<CreateGroupResponse> call, Throwable t) {
                mBus.post(new CreateGroupEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void getGroup (GetMembersForGroupEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().getGroupMembers(onLoadingStart.getRequest().getToken(), onLoadingStart.getRequest().getGroupID()).enqueue(new Callback<GetGroupMembersResponse>() {
            @Override
            public void onResponse(Call<GetGroupMembersResponse> call, Response<GetGroupMembersResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new GetMembersForGroupEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new GetMembersForGroupEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<GetGroupMembersResponse> call, Throwable t) {
                mBus.post(new GetMembersForGroupEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void joinGroup (JoinGroupEvent.OnLoadingStart onLoadingStart) {
        mApiClient.getSmooviesApiService().joinGroup(onLoadingStart.getRequest()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new JoinGroupEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new JoinGroupEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mBus.post(new JoinGroupEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void leaveGroup (LeaveGroupEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().leaveGroup(onLoadingStart.getRequest()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new LeaveGroupEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new LeaveGroupEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mBus.post(new LeaveGroupEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void getEvents (GetEventsEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().getMyEvents(onLoadingStart.getRequest().getToken()).enqueue(new Callback<GetEventsResponse>() {
            @Override
            public void onResponse(Call<GetEventsResponse> call, Response<GetEventsResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new GetEventsEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new GetEventsEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<GetEventsResponse> call, Throwable t) {
                mBus.post(new GetEventsEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void getAttendeesForEvent (GetAttendeesEvent.OnLoadingStart onLoadingStart){

        mApiClient.getSmooviesApiService().getAttendeesForEvent(onLoadingStart.getRequest().getToken(), onLoadingStart.getRequest().getEventID()).enqueue(new Callback<GetAttendeesResponse>() {
            @Override
            public void onResponse(Call<GetAttendeesResponse> call, Response<GetAttendeesResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new GetAttendeesEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new GetAttendeesEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<GetAttendeesResponse> call, Throwable t) {
                mBus.post(new GetAttendeesEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void getEventDetails (GetEventDetailEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().getEventDetails(onLoadingStart.getRequest().getToken(), onLoadingStart.getRequest().getEventID()).enqueue(new Callback<GetEventDetailResponse>() {
            @Override
            public void onResponse(Call<GetEventDetailResponse> call, Response<GetEventDetailResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new GetEventDetailEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new GetEventDetailEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<GetEventDetailResponse> call, Throwable t) {
                mBus.post(new GetEventDetailEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void getEventsForGroup (GetEventsForGroupEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().getEventsForGroup(onLoadingStart.getRequest().getToken(), onLoadingStart.getRequest().getGroupID()).enqueue(new Callback<GetEventsResponse>() {
            @Override
            public void onResponse(Call<GetEventsResponse> call, Response<GetEventsResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new GetEventsForGroupEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new GetEventsForGroupEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<GetEventsResponse> call, Throwable t) {
                mBus.post(new GetEventsForGroupEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void getGroupDetails (GetGroupDetailsEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().getGroupDetails(onLoadingStart.getRequest().getToken(), onLoadingStart.getRequest().getGroupID()).enqueue(new Callback<GetGroupDetailsResponse>() {
            @Override
            public void onResponse(Call<GetGroupDetailsResponse> call, Response<GetGroupDetailsResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new GetGroupDetailsEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new GetGroupDetailsEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<GetGroupDetailsResponse> call, Throwable t) {
                mBus.post(new GetGroupDetailsEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void createEvent (CreateEventEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().createEvent(onLoadingStart.getRequest()).enqueue(new Callback<CreateEventResponse>() {
            @Override
            public void onResponse(Call<CreateEventResponse> call, Response<CreateEventResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new CreateEventEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new CreateEventEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<CreateEventResponse> call, Throwable t) {
                mBus.post(new CreateEventEvent.OnLoadingError("error!", -1));
            }
        });
    }

    @Subscribe
    public void disbandGroup (DisbandGroupEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().disbandGroup(onLoadingStart.getRequest()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new DisbandGroupEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new DisbandGroupEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mBus.post(new DisbandGroupEvent.OnLoadingError("error!", -1));
            }
        });
    }


    @Subscribe
    public void disbandGroup (CancelEventEvent.OnLoadingStart onLoadingStart){
        mApiClient.getSmooviesApiService().cancelEvent(onLoadingStart.getRequest()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    mBus.post(new CancelEventEvent.OnLoaded(response.body()));
                } else {
                    mBus.post(new CancelEventEvent.OnLoadingError("", 406));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                mBus.post(new CancelEventEvent.OnLoadingError("error!", -1));
            }
        });
    }
}
