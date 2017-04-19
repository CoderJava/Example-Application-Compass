package com.ysn.exampleapplicationcompass.views.main;

import com.ysn.exampleapplicationcompass.views.base.View;

/**
 * Created by root on 17/04/17.
 */

public interface MainActivityView extends View {

    void adjustArrow(float currentAzimuth, float azimuth);

    void setLocationNameSuccess(String formattedAddress);

    void setLocationNameFail();

    void showDialogEnabledGps();

    void gpsAlreadyEnabled();

    void gotoMyLocationActivity();

    void gotoMapsActivity();
}
