package com.ysn.exampleapplicationcompass.library.gps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.MapView;
import com.ysn.exampleapplicationcompass.views.base.View;
import com.ysn.exampleapplicationcompass.views.main.MainActivity;
import com.ysn.exampleapplicationcompass.views.main.MainActivityPresenter;
import com.ysn.exampleapplicationcompass.views.main.MainActivityView;
import com.ysn.exampleapplicationcompass.views.submenu.location.MyLocationActivityView;

/**
 * Created by root on 19/04/17.
 */

public class GPSTracker implements LocationListener {

    private static final String TAG = "GPSTracker";
    private final Context context;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for can get location
    boolean canGetLocation = false;

    // flag for view
    View view;

    Location location;
    double latitude;
    double longitude;

    // the minimum distance to change update in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 1 meter

    // the minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute

    // declaring a location manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.context = context;
    }

    public Location getLocation(View view) {
        this.view = view;
        try {
            Log.d(TAG, "getLocation");
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                // GPS not enabled
                Log.d(TAG, "GPS not enabled");
                onProviderDisabled("gps");
                return null;
            } else if (!isNetworkEnabled) {
                // Network not enabled
                Log.d(TAG, "Network not enabled");
                onProviderDisabled("network");
                return null;
            } else {
                this.canGetLocation = true;

                // get location from network provider
                if (isNetworkEnabled) {
                    Log.d(TAG, "isNetworkEnabled: " + isNetworkEnabled);
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    // stop using gps listener
    public void stopUsingGps() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        if (view instanceof MainActivityView) {
            MainActivityView mainActivityView = (MainActivityView) view;
            mainActivityView.refreshLocation(location);
        } else if (view instanceof MyLocationActivityView) {
            MyLocationActivityView myLocationActivityView = (MyLocationActivityView) view;
            myLocationActivityView.refreshLocation(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG, "onProviderDisabled: " + s);
        if (s.equalsIgnoreCase("gps")) {
            if (view instanceof MainActivityView) {
                MainActivityView mainActivityView = (MainActivityView) view;
                mainActivityView.showDialogEnabledGps();
            } else if (view instanceof MyLocationActivityView) {
                MyLocationActivityView myLocationActivityView = (MyLocationActivityView) view;
                myLocationActivityView.showEnabledGps();
            }
        } else if (s.equalsIgnoreCase("network")) {
            if (view instanceof MainActivityView) {
                MainActivityView mainActivityView = (MainActivityView) view;
                mainActivityView.showDialogEnabledGps();
            } else if (view instanceof MyLocationActivityView) {
                MyLocationActivityView myLocationActivityView = (MyLocationActivityView) view;
                myLocationActivityView.showEnabledGps();
            }
        }
    }
}
