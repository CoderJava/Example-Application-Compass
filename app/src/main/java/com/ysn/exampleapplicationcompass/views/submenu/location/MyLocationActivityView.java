package com.ysn.exampleapplicationcompass.views.submenu.location;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ysn.exampleapplicationcompass.views.base.View;

/**
 * Created by root on 19/04/17.
 */

public interface MyLocationActivityView extends View {

    void loadDataGps(LatLng latLngLastLocation);

    void drawPolyline(PolylineOptions polylineOptions);

    void drawPolylineFailed();

    void refreshLocation(Location locationRefresh);

    void showEnabledGps();

    void refreshLocationFailed();

    void getDataDistanceMatrixFailed();

    void getDataDistanceMatrixSuccess(String distance, String duration);
}
