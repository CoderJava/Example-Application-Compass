package com.ysn.exampleapplicationcompass.services;

import android.Manifest;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.ysn.exampleapplicationcompass.views.main.MainActivityPresenter;

/**
 * Created by root on 18/04/17.
 */

public class LocationGcmTaskService extends GcmTaskService {

    private static final String TAG = "LocationGcmTaskService";
    public static LocationRequest locationRequest;
    public static GoogleApiClient googleApiClient;
    public static Context context;

    @Override
    public int onRunTask(TaskParams taskParams) {
        switch (taskParams.getTag()) {
            case MainActivityPresenter.PERIODIC_TASK:
                return doPeriodicTask();
            default:
                return 0;
        }
    }

    private int doPeriodicTask() {
        Log.d(TAG, "doPeriodicTask");
        Location lastLocation = null;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        LatLng latLngLastLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        MainActivityPresenter.getLocationNameNow(latLngLastLocation);
        return 1;
    }
}
