package com.ysn.exampleapplicationcompass.api;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

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

    /*https://maps.googleapis.com/maps/api/directions/json?origin=...&destination=...&key=...*/
    /*@GET("maps/api/directions/json")
    Call<ResponseBody> getDirection(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key
    );*/

    /*https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=3.5763926,98.6824935&destinations=3.581256655683292,98.68268173187971&key=AIzaSyCWel6yfkJ_PMYql_REc60Aikc6beLIYAA*/
    /*@GET("maps/api/distancematrix/json")
    Call<ResponseBody> getDistanceMatrix(
            @Query("units") String units,
            @Query("origins") String origin,
            @Query("destination") String destination,
            @Query("key") String key
    );*/

    @GET("maps/api/directions/json")
    Observable<JsonObject> getDirection(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key
    );

    @GET("maps/api/distancematrix/json")
    Observable<JsonObject> getDistanceMatrix(
            @Query("units") String units,
            @Query("origins") String origin,
            @Query("destinations") String destination,
            @Query("key") String key
    );

}
