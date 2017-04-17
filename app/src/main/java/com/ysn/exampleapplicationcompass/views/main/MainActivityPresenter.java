package com.ysn.exampleapplicationcompass.views.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.widget.TabHost;

import com.ysn.exampleapplicationcompass.library.Compass;
import com.ysn.exampleapplicationcompass.views.base.Presenter;

/**
 * Created by root on 17/04/17.
 */

public class MainActivityPresenter implements Presenter<MainActivityView> {
    private static final String TAG = "MainActivityPresenter";
    MainActivityView mainActivityView;
    Compass compass;

    @Override
    public void onAttach(MainActivityView view) {
        mainActivityView = view;
    }

    public void initCompass(Context context) {
        if (compass == null) {
            compass = new Compass(context);
        }
        compass.start(MainActivityPresenter.this, "MainActivity");
    }

    @Override
    public void onDetach() {
        mainActivityView = null;
        compass.stop();
    }

    public void onAdjustArrow(float currentAzimuth, float azimuth) {
        if (mainActivityView == null) {
            Log.d(TAG, "mainActivityView == null");
        }
        mainActivityView.adjustArrow(currentAzimuth, azimuth);
    }

}
