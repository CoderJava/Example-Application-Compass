package com.ysn.exampleapplicationcompass.views.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.ysn.exampleapplicationcompass.api.GoogleMapsApiService;
import com.ysn.exampleapplicationcompass.library.Compass;
import com.ysn.exampleapplicationcompass.services.LocationGcmTaskService;
import com.ysn.exampleapplicationcompass.views.base.Presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 17/04/17.
 */

public class MainActivityPresenter implements Presenter<MainActivityView>, LocationListener {
    private static final String TAG = "MainActivityPresenter";
    public static final String PERIODIC_TASK = "PERIODIC_TASK";
    private static MainActivityView mainActivityView;
    private Compass compass;
    private Location lastLocation;
    private static Retrofit retrofit;
    private GcmNetworkManager gcmNetworkManager;

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
        stopCompass();
    }

    public void stopCompass() {
        compass.stop();
    }

    public void onAdjustArrow(float currentAzimuth, float azimuth) {
        if (mainActivityView == null) {
            Log.d(TAG, "mainActivityView == null");
        }
        mainActivityView.adjustArrow(currentAzimuth, azimuth);
    }

    public void onConnected(Context context, GoogleApiClient googleApiClient) {
        Log.d(TAG, "onConnected");
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // permission for marshmellow
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LocationGcmTaskService.locationRequest = locationRequest;
        LocationGcmTaskService.googleApiClient = googleApiClient;
        LocationGcmTaskService.context = context;

        // start service location gcm task
        startLocationGcmTaskService(context);

        if (lastLocation == null) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Log.d(TAG, "lastLocation == null");
        } else {
            Log.d(TAG, "lastLocation: " + lastLocation);
            LatLng latLnglastLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            initRetrofit(GoogleMapsApiService.baseApiUrl);
            GoogleMapsApiService googleMapsApiService = retrofit.create(GoogleMapsApiService.class);
            String strLatlngLocationNow = latLnglastLocation.latitude + ", " + latLnglastLocation.longitude;
            Log.d(TAG, "strLatlngLocationNow: " + strLatlngLocationNow);
            Call<ResponseBody> resultCallGetLocationNameNow = googleMapsApiService.getLocationNameNow(strLatlngLocationNow, GoogleMapsApiService.apiKey);
            resultCallGetLocationNameNow.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObjectDataLocationNameNow = new JSONObject(response.body().string());
                        String status = jsonObjectDataLocationNameNow.getString("status");
                        if (status.equals("OK")) {
                            Log.d(TAG, "onResponse: " + status);
                            JSONArray jsonArrayArrayResults = jsonObjectDataLocationNameNow.getJSONArray("results");
                            JSONObject jsonObjectItemResults = jsonArrayArrayResults.getJSONObject(0);
                            String formattedAddress = jsonObjectItemResults.getString("formatted_address");
                            Log.d(TAG, "formattedAddress: " + formattedAddress);
                            mainActivityView.setLocationNameSuccess(formattedAddress);
                        } else {
                            mainActivityView.setLocationNameFail();
                            Log.d(TAG, "onResponse: " + status);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    mainActivityView.setLocationNameFail();
                    Log.d(TAG, "onFailure");
                }
            });
        }
    }

    private void startLocationGcmTaskService(Context context) {
        gcmNetworkManager = GcmNetworkManager.getInstance(context);
        /*OneoffTask oneoffTask = new OneoffTask.Builder()
                .setService(LocationGcmTaskService.class)
                .setExecutionWindow(0, 60)
                .setTag(PERIODIC_TASK)
                .setRequiredNetwork(Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .build();*/
        PeriodicTask periodicTask = new PeriodicTask.Builder()
                .setService(LocationGcmTaskService.class)
                .setTag(PERIODIC_TASK)
                .setPeriod(30) // seconds
                .build();
        gcmNetworkManager.schedule(periodicTask);
    }

    private static void initRetrofit(String baseApiUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void onCheckPermissionGps(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // please, enabled GPS or Location in devices
            mainActivityView.showDialogEnabledGps();
        } else {
            // GPS already enabled in devices
            mainActivityView.gpsAlreadyEnabled();
        }
    }

    public static void getLocationNameNow(LatLng latLngLastLocation) {
        initRetrofit(GoogleMapsApiService.baseApiUrl);
        GoogleMapsApiService googleMapsApiService = retrofit.create(GoogleMapsApiService.class);
        String strLatlngLastLocation = latLngLastLocation.latitude + ", " + latLngLastLocation.longitude;
        Call<ResponseBody> resultCallGetLocationNameNow = googleMapsApiService.getLocationNameNow(strLatlngLastLocation, GoogleMapsApiService.apiKey);
        resultCallGetLocationNameNow.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObjectResponse = new JSONObject(response.body().string());
                    if (jsonObjectResponse.getString("status").equalsIgnoreCase("OK")) {
                        // response success
                        JSONArray jsonArrayResults = jsonObjectResponse.getJSONArray("results");
                        JSONObject jsonObjectItemResult = jsonArrayResults.getJSONObject(0);
                        String formattedAddress = jsonObjectItemResult.getString("formatted_address");
                        mainActivityView.setLocationNameSuccess(formattedAddress);
                    } else {
                        // response success but no data
                        mainActivityView.setLocationNameFail();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mainActivityView.setLocationNameFail();
                } catch (IOException e) {
                    e.printStackTrace();
                    mainActivityView.setLocationNameFail();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                mainActivityView.setLocationNameFail();
            }
        });
    }
}
