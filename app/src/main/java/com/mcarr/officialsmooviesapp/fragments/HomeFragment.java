package com.mcarr.officialsmooviesapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mcarr.officialsmooviesapp.R;
import com.mcarr.officialsmooviesapp.activities.MyProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**********************************
 * Created by Mikel on 30-May-16.
 *********************************/
public class HomeFragment extends ParentFragment {

    /** Views **/
    @BindView(R.id.fragment_home_profile_layout) LinearLayout mProfileLayout;

    /** Instance **/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //((DaggerApplication) getActivity().getApplication()).getNetComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @OnClick(R.id.fragment_home_profile_layout) void onProfileClicked() {
        Intent intent = new Intent(getActivity(), MyProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        //BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //BusProvider.getInstance().register(this);
        //BaseRequest tokenRequest = new BaseRequest(mAuthKey);
        //BusProvider.getInstance().post(new GetEventsEvent.OnLoadingStart(tokenRequest));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

    }

}
