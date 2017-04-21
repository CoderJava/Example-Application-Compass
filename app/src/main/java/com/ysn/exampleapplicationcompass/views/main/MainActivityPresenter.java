package com.ysn.exampleapplicationcompass.views.main;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.maps.model.LatLng;
import com.ysn.exampleapplicationcompass.api.GoogleMapsApiService;
import com.ysn.exampleapplicationcompass.library.gps.Compass;
import com.ysn.exampleapplicationcompass.library.gps.GPSTracker;
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

public class MainActivityPresenter implements Presenter<MainActivityView> {

    private static final String TAG = "MainActivityPresenter";
    private static MainActivityView mainActivityView;
    private Compass compass;
    private static Retrofit retrofit;
    private GPSTracker gpsTracker;
    private Context context;

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

    public void onConnected(Context context, Location locationRefresh) {
        this.context = context;
        Log.d(TAG, "onConnected");
        if (gpsTracker == null) {
            gpsTracker = new GPSTracker(context);
            locationRefresh = gpsTracker.getLocation(mainActivityView);
        }
        Log.d(TAG, "by GPSTracker latitude: " + locationRefresh.getLatitude() + ", longitude: " + locationRefresh.getLongitude());
        final LatLng latLnglastLocation = new LatLng(locationRefresh.getLatitude(), locationRefresh.getLongitude());
        initRetrofit(GoogleMapsApiService.baseApiUrl);
        GoogleMapsApiService googleMapsApiService = retrofit.create(GoogleMapsApiService.class);
        String strLatlngLocationNow = latLnglastLocation.latitude + ", " + latLnglastLocation.longitude;
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
                        mainActivityView.setLocationNameSuccess(formattedAddress, latLnglastLocation);
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

    private static void initRetrofit(String baseApiUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void onGotoMyLocationActivity() {
        mainActivityView.gotoMyLocationActivity();
    }

    public void onGotoMapsActivity() {
        mainActivityView.gotoMapsActivity();
    }

    public void startGpsService(Context context) {
        if (gpsTracker == null) {
            gpsTracker = new GPSTracker(context);
        }
        gpsTracker.getLocation(mainActivityView);
    }

    public void stopGpsService() {
        gpsTracker.stopUsingGps();
        Log.d(TAG, "stopGpsService in MainActivityPresenter");
    }

}
