package com.ysn.exampleapplicationcompass.views.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TabHost;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.ysn.exampleapplicationcompass.api.GoogleMapsApiService;
import com.ysn.exampleapplicationcompass.internal.model.geocode.Geocode;
import com.ysn.exampleapplicationcompass.library.Compass;
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
    private MainActivityView mainActivityView;
    private Compass compass;
    private Location lastLocation;
    private Retrofit retrofit;

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

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation == null) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            Log.d(TAG, "lastLocation == null");
        } else {
            Log.d(TAG, "lastLocation: " + lastLocation);
            LatLng latLnglastLoation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            // do something
            initRetrofit(GoogleMapsApiService.baseApiUrl);
            GoogleMapsApiService googleMapsApiService = retrofit.create(GoogleMapsApiService.class);
            String strLatlngLocationNow = latLnglastLoation.latitude + ", " + latLnglastLoation.longitude;
            Log.d(TAG, "strLatlngLocationNow: " + strLatlngLocationNow);
            Call<ResponseBody> resultCallGetLocationNameNow = googleMapsApiService.getLocationNameNow(strLatlngLocationNow, GoogleMapsApiService.apiKey);
            resultCallGetLocationNameNow.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject jsonObjectDataLocationNameNow = new JSONObject(response.body().string());
                        String status = jsonObjectDataLocationNameNow.getString("status");
                        if (status.equals("OK")) {
                            // do something
                            Log.d(TAG, "onResponse: " + status);
                            JSONArray jsonArrayArrayResults = jsonObjectDataLocationNameNow.getJSONArray("results");
                            JSONObject jsonObjectItemResults = jsonArrayArrayResults.getJSONObject(0);
                            String formattedAddress = jsonObjectItemResults.getString("formatted_address");
                            Log.d(TAG, "formattedAddress: " + formattedAddress);
                            mainActivityView.setLocationNameSuccess(formattedAddress);
                        } else {
                            // do something
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

    private void initRetrofit(String baseApiUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
