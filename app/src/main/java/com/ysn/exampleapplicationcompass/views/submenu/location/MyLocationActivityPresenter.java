package com.ysn.exampleapplicationcompass.views.submenu.location;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;
import com.ysn.exampleapplicationcompass.api.GoogleMapsApiService;
import com.ysn.exampleapplicationcompass.internal.model.location.DirectionAndDistanceMatrix;
import com.ysn.exampleapplicationcompass.library.gps.GPSTracker;
import com.ysn.exampleapplicationcompass.library.maps.DataParser;
import com.ysn.exampleapplicationcompass.views.base.Presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by root on 19/04/17.
 */

public class MyLocationActivityPresenter implements Presenter<MyLocationActivityView> {

    private final String TAG = "MyLocationPresenterTAG";
    private static MyLocationActivityView myLocationActivityView;
    private GPSTracker gpsTracker;
    private Retrofit retrofit;

    @Override
    public void onAttach(MyLocationActivityView view) {
        myLocationActivityView = view;
    }

    @Override
    public void onDetach() {
        myLocationActivityView = null;
    }

    public void onLoadDataGps(Context context) {
        if (gpsTracker == null) {
            gpsTracker = new GPSTracker(context);
        }
        Location lastLocation = gpsTracker.getLocation(myLocationActivityView);
        LatLng latLngLastLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        myLocationActivityView.loadDataGps(latLngLastLocation);
    }


    /*public void drawRouteMaps(String strLatlngOrigin, String strLatlngDestination) {
        initRetrofit(GoogleMapsApiService.baseApiUrl);
        final GoogleMapsApiService googleMapsApiService = retrofit.create(GoogleMapsApiService.class);
        Call<ResponseBody> resultCallGetDirection = googleMapsApiService.getDirection(strLatlngOrigin, strLatlngDestination, GoogleMapsApiService.apiKey);
        resultCallGetDirection.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObjectResponse = new JSONObject(response.body().string());
                    String status = jsonObjectResponse.getString("status");
                    if (status.equalsIgnoreCase("OK")) {
                        Log.d(TAG, "onResponse: OK");
                        DataParser dataParser = new DataParser();
                        List<List<HashMap<String, String>>> routes = dataParser.parser(jsonObjectResponse);
                        ArrayList<LatLng> points;
                        PolylineOptions polylineOptions = new PolylineOptions();

                        // traversing through all the routes
                        for(int a = 0; a < routes.size(); a++) {
                            points = new ArrayList<LatLng>();

                            // fetching i-th route
                            List<HashMap<String, String>> path = routes.get(a);

                            // fetching all the points in i-th route
                            for(int b = 0; b < path.size(); b++) {
                                HashMap<String, String> point = path.get(b);

                                double latitude = Double.parseDouble(point.get("lat"));
                                double longitude = Double.parseDouble(point.get("lng"));
                                LatLng latLngPosition = new LatLng(latitude, longitude);
                                points.add(latLngPosition);
                            }

                            // adding all the points in the route to LineOptions
                            polylineOptions.addAll(points);
                            polylineOptions.width(5);
                            polylineOptions.color(Color.RED);
                        }
                        myLocationActivityView.drawPolyline(polylineOptions);
                    } else {
                        // can not drawing polyline
                        myLocationActivityView.drawPolylineFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    myLocationActivityView.drawPolylineFailed();
                } catch (Exception e) {
                    e.printStackTrace();
                    myLocationActivityView.drawPolylineFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                myLocationActivityView.drawPolylineFailed();
            }
        });
    }*/

    public void drawRouteMaps(String strLatlngOrigin, String strLatlngDestination) {
        // declare retrofit
        initRetrofit(GoogleMapsApiService.baseApiUrl);

        // direction
        Observable<JsonObject> observableDirection = retrofit
                .create(GoogleMapsApiService.class)
                .getDirection(strLatlngOrigin, strLatlngDestination, GoogleMapsApiService.apiKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        // matrix distance
        Observable<JsonObject> observableMatrixDistance = retrofit
                .create(GoogleMapsApiService.class)
                .getDistanceMatrix("imperial", strLatlngOrigin, strLatlngDestination, GoogleMapsApiService.apiKey)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        // combined result data direction and matrix distance
        Observable<DirectionAndDistanceMatrix> observableDirectionAndDistanceMatrix = Observable.zip(observableDirection, observableMatrixDistance, new Func2<JsonObject, JsonObject, DirectionAndDistanceMatrix>() {
            @Override
            public DirectionAndDistanceMatrix call(JsonObject jsonObjectDirection, JsonObject jsonObjectDistanceMatrix) {
                return new DirectionAndDistanceMatrix(jsonObjectDirection, jsonObjectDistanceMatrix);
            }
        });
        observableDirectionAndDistanceMatrix.subscribe(new Subscriber<DirectionAndDistanceMatrix>() {
            @Override
            public void onCompleted() {
                // nothing to do in here
                Log.d(TAG, "onCompleted in observableDirectionAndDistanceMatrix");
            }

            @Override
            public void onError(Throwable e) {
                // nothing to do in here
                Log.d(TAG, "onError in observableDirectionAndDistanceMatrix");
            }

            @Override
            public void onNext(DirectionAndDistanceMatrix directionAndDistanceMatrix) {
                // data direction
                try {
                    Log.d(TAG, "onNext in observableDirectionAndDistanceMatrix\njsonObjectDiretion: " + directionAndDistanceMatrix.getJsonObjectDirection() + ", jsonObjectDistanceMatrix: " + directionAndDistanceMatrix.getJsonObjectDistanceMatrix());
                    JsonObject jsonObjectDirectionTemp = directionAndDistanceMatrix.getJsonObjectDirection();
                    JSONObject jsonObjectDirection = new JSONObject(jsonObjectDirectionTemp.toString());
                    if (jsonObjectDirection == null) {
                        myLocationActivityView.refreshLocationFailed();
                        return;
                    } else {
                        try {
                            String status = jsonObjectDirection.getString("status");
                            if (status.equalsIgnoreCase("OK")) {
                                // response success
                                DataParser dataParser = new DataParser();
                                List<List<HashMap<String, String>>> routes = dataParser.parser(jsonObjectDirection);
                                ArrayList<LatLng> points;
                                PolylineOptions polylineOptions = new PolylineOptions();

                                // traversing through all the routes
                                for (int a = 0; a < routes.size(); a++) {
                                    points = new ArrayList<LatLng>();

                                    // fetching i-th route
                                    List<HashMap<String, String>> path = routes.get(a);

                                    // fetching all the points in i-th route
                                    for (int b = 0; b < path.size(); b++) {
                                        HashMap<String, String> point = path.get(b);

                                        double latitude = Double.parseDouble(point.get("lat"));
                                        double longitude = Double.parseDouble(point.get("lng"));
                                        LatLng latLngPosition = new LatLng(latitude, longitude);
                                        points.add(latLngPosition);
                                    }

                                    // adding all the points in the route to LineOptions
                                    polylineOptions.addAll(points);
                                    polylineOptions.width(5);
                                    polylineOptions.color(Color.RED);

                                    myLocationActivityView.drawPolyline(polylineOptions);
                                }
                            } else {
                                // response failed
                                myLocationActivityView.drawPolylineFailed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            myLocationActivityView.drawPolylineFailed();
                        } catch (Exception e) {
                            e.printStackTrace();
                            myLocationActivityView.drawPolylineFailed();
                        }

                    }

                    JsonObject jsonObjectDistanceMatrixTemp = directionAndDistanceMatrix.getJsonObjectDistanceMatrix();
                    JSONObject jsonObjectDistanceMatrix = new JSONObject(jsonObjectDistanceMatrixTemp.toString());
                    String distance = "";
                    String duration = "";
                    if (jsonObjectDistanceMatrix == null) {
                        myLocationActivityView.getDataDistanceMatrixFailed();
                    } else {
                        try {
                            String status = jsonObjectDistanceMatrix.getString("status");
                            if (status.equalsIgnoreCase("OK")) {
                                // response success
                                JSONArray jsonArrayRows = jsonObjectDistanceMatrix.getJSONArray("rows");
                                if (jsonArrayRows.length() > 0) {
                                    JSONArray jsonArrayElements = jsonArrayRows.getJSONObject(0).getJSONArray("elements");
                                    if (jsonArrayElements.length() > 0) {
                                        JSONObject jsonObjectDistance = jsonArrayElements.getJSONObject(0).getJSONObject("distance");
                                        distance = jsonObjectDistance.getString("text");

                                        JSONObject jsonObjectDuration = jsonArrayElements.getJSONObject(0).getJSONObject("duration");
                                        duration = jsonObjectDuration.getString("text");

                                        myLocationActivityView.getDataDistanceMatrixSuccess(distance, duration);
                                    } else {
                                        myLocationActivityView.getDataDistanceMatrixFailed();
                                    }
                                }
                            } else {
                                // response failed
                                myLocationActivityView.getDataDistanceMatrixFailed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            myLocationActivityView.getDataDistanceMatrixFailed();
                        } catch (Exception e) {
                            e.printStackTrace();
                            myLocationActivityView.getDataDistanceMatrixFailed();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initRetrofit(String baseApiUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void startGpsService(Context context) {
        if (gpsTracker == null) {
            gpsTracker = new GPSTracker(context);
        }
        gpsTracker.getLocation(myLocationActivityView);
        Log.d(TAG, "startGpsService in MyLocationActivityPresenter");
    }

    public void stopGpsService() {
        Log.d(TAG, "stopGpsService in MyLocationActivityPresenter");
    }

}
