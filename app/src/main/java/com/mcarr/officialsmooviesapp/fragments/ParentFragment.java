package com.mcarr.officialsmooviesapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**********************************
 * Created by Mikel on 12-May-16.
 *********************************/
public abstract class ParentFragment extends Fragment {

    private static final String LOGTAG = "ParentFragment";
    protected ViewGroup mRootView;

    private static final Field sChildFragmentManagerField;

    static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            Log.e(LOGTAG, "Error getting mChildFragmentManager field", e);
        }
        sChildFragmentManagerField = f;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {
                Log.e(LOGTAG, "Error setting mChildFragmentManager field", e);
            }
        }
    }

    protected void start() {}
    protected void init(Bundle savedInstanceState) {}
    protected void findViews(){}
    protected void setUpViews(){}

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        start();
        init(savedInstanceState);
        findViews();
        setUpViews();
    }

    protected View findViewById(int id){
        return mRootView.findViewById(id);
    }
}