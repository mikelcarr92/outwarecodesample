package com.mcarr.officialsmooviesapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.preference.PreferenceManager;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.retrofit.ISmooviesAPI;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**********************************
 * Created by Mikel on 21-May-16.
 *********************************/
public class Util {

    public static ISmooviesAPI getApi() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ISmooviesAPI.class);
    }

    public static int calculateColorFromString(String string) {
        String opacity = "#ff"; //opacity between 00-ff
        String hexColor = String.format(
                opacity + "%06X", (0xeeeeee & string.hashCode()));

        return Color.parseColor(hexColor);
    }

    public static String getToken(Context context) {
        //SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.AUTH_KEY, "null");
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static boolean colorWillBeMasked(int color){
        if(android.R.color.transparent == color) return true;

        int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};

        int brightness =
                (int)Math.sqrt(
                        rgb[0] * rgb[0] * .241 +
                                rgb[1] * rgb[1] * .691 +
                                rgb[2] * rgb[2] * .068);

        System.out.println("COLOR: " + color + ", BRIGHT: " + brightness);
        //note 0,black 1,classic 2
        //int theme = app.api.getThemeView();

        // color is dark
        if(brightness <= 40){
            return true;
        }
        // color is light
        else if (brightness >= 215){
            return true;
        }
        return false;
    }

}
