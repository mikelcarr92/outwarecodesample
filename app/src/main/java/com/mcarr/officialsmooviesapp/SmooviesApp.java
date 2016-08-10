package com.mcarr.officialsmooviesapp;

import android.app.Application;

import com.mcarr.officialsmooviesapp.bus.BusProvider;
import com.mcarr.officialsmooviesapp.retrofit.ApiRequestHandler;
import com.squareup.otto.Bus;

import net.danlew.android.joda.JodaTimeAndroid;

/**********************************
 * Created by Mikel on 23-May-16.
 *********************************/
public class SmooviesApp extends Application {

    public static ApiRequestHandler mApiRequestHandler;
    private Bus mBus = BusProvider.getInstance();

    @Override public void onCreate() {
        super.onCreate();
        mApiRequestHandler = new ApiRequestHandler(mBus);
        mBus.register(mApiRequestHandler);
        JodaTimeAndroid.init(this);
    }

}
