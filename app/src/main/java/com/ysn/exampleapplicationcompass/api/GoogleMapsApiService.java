package com.ysn.exampleapplicationcompass.api;

import com.ysn.exampleapplicationcompass.internal.model.geocode.Geocode;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by root on 17/04/17.
 */

public interface GoogleMapsApiService {
    public final String baseApiUrl = "https://maps.googleapis.com/";
    public final String apiKey = "AIzaSyAFB_yc7p3i4kou8m6jPjKqpcDHrX0uuKk";

    /*https://maps.googleapis.com/maps/api/geocode/json?latlng=3.5951956, 98.67222270000001&key=AIzaSyAFB_yc7p3i4kou8m6jPjKqpcDHrX0uuKk*/
    @GET("maps/api/geocode/json")
    Call<ResponseBody> getLocationNameNow(
            @Query("latlng") String latlng,
            @Query("key") String key
    );

}
