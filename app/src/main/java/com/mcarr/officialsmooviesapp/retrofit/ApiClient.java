package com.mcarr.officialsmooviesapp.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcarr.officialsmooviesapp.util.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public class ApiClient {

    private static ApiClient mApiClient;
    private static Retrofit mRetrofit;
    private ISmooviesAPI mService;

    public static ApiClient getClient() {
        if (mApiClient == null)
            mApiClient = new ApiClient();
        return mApiClient;
    }

    private ApiClient() {
        Gson gson = new GsonBuilder().create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mService = mRetrofit.create(ISmooviesAPI.class);
    }

    public ISmooviesAPI getSmooviesApiService() {
        return mService;
    }
}
