package com.mcarr.officialsmooviesapp.retrofit;

import com.mcarr.officialsmooviesapp.object.request.CancelEventRequest;
import com.mcarr.officialsmooviesapp.object.request.CreateEventRequest;
import com.mcarr.officialsmooviesapp.object.request.CreateGroupRequest;
import com.mcarr.officialsmooviesapp.object.request.GroupRequest;
import com.mcarr.officialsmooviesapp.object.request.JoinGroupRequest;
import com.mcarr.officialsmooviesapp.object.request.LeaveGroupRequest;
import com.mcarr.officialsmooviesapp.object.request.SetProfileImageRequest;
import com.mcarr.officialsmooviesapp.object.response.BaseResponse;
import com.mcarr.officialsmooviesapp.object.response.CreateEventResponse;
import com.mcarr.officialsmooviesapp.object.response.CreateGroupResponse;
import com.mcarr.officialsmooviesapp.object.response.GetAttendeesResponse;
import com.mcarr.officialsmooviesapp.object.response.GetEventDetailResponse;
import com.mcarr.officialsmooviesapp.object.response.GetEventsResponse;
import com.mcarr.officialsmooviesapp.object.response.GetGroupDetailsResponse;
import com.mcarr.officialsmooviesapp.object.response.GetGroupMembersResponse;
import com.mcarr.officialsmooviesapp.object.response.LoginResponse;
import com.mcarr.officialsmooviesapp.object.response.GroupsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**********************************
 * Created by Mikel on 20-May-16.
 *********************************/
public interface ISmooviesAPI {

    @FormUrlEncoded
    @POST("services/login")
    Call<LoginResponse> login(@Field("email")String email, @Field("password")String password);

    @FormUrlEncoded
    @POST("services/createAccount")
    Call<LoginResponse> createAccount(@Field("username")String username, @Field("email")String email, @Field("password")String password);

    @GET("services/getMyGroups")
    Call<GroupsResponse> getMyGroups(@Query("token") String token);

    @GET("services/getUnjoinedGroups")
    Call<GroupsResponse> getUnjoinedGroups(@Query("token") String token);

    @POST("services/createGroup")
    Call<CreateGroupResponse> createGroup(@Body CreateGroupRequest request);

    @GET("services/getGroupMembers")
    Call<GetGroupMembersResponse> getGroupMembers(@Query("token") String token, @Query("groupID") int groupID);

    @POST("services/leaveGroup")
    Call<BaseResponse> leaveGroup(@Body LeaveGroupRequest request);

    @GET("services/getMyEvents")
    Call<GetEventsResponse> getMyEvents(@Query("token") String token);

    @GET("services/getAttendeesForEvent")
    Call<GetAttendeesResponse> getAttendeesForEvent(@Query("token") String token, @Query("eventID") int eventID);

    @GET("services/getEventDetails")
    Call<GetEventDetailResponse> getEventDetails(@Query("token") String token, @Query("eventID") int eventID);

    @GET("services/getEventsForGroup")
    Call<GetEventsResponse> getEventsForGroup(@Query("token") String token, @Query("groupID") int groupID);

    @POST("services/joinGroup")
    Call<BaseResponse> joinGroup(@Body JoinGroupRequest request);

    @GET("services/getGroupDetails")
    Call<GetGroupDetailsResponse> getGroupDetails(@Query("token") String token, @Query("groupID") int groupID);

    @POST("services/createEvent")
    Call<CreateEventResponse> createEvent(@Body CreateEventRequest request);

    @POST("services/disbandGroup")
    Call<BaseResponse> disbandGroup(@Body GroupRequest request);

    @POST("services/setProfileImage")
    Call<BaseResponse> setProfileImage(@Body SetProfileImageRequest request);

    @POST("services/deleteEvent")
    Call<BaseResponse> cancelEvent(@Body CancelEventRequest request);

}
