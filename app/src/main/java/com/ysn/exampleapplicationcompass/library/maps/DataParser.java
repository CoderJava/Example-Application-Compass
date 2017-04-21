package com.ysn.exampleapplicationcompass.library.maps;

import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 20/04/17.
 */

public class DataParser {

    private static final String TAG = "DataParserTAG";
    /*
        * Receive a JSONObject and returns a list of lists containing latitude and longitude
        * */
    public List<List<HashMap<String, String>>> parser(JSONObject jsonObject) {
        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jsonArrayRoutes;
        JSONArray jsonArrayLegs;
        JSONArray jsonArraySteps;

        try {
            jsonArrayRoutes = jsonObject.getJSONArray("routes");

            // Traversing all routes
            for (int a = 0; a < jsonArrayRoutes.length(); a++) {
                jsonArrayLegs = ((JSONObject) jsonArrayRoutes.get(a)).getJSONArray("legs");
                List path = new ArrayList();

                // traversing all legs
                for (int b = 0; b < jsonArrayLegs.length(); b++) {
                    jsonArraySteps = ((JSONObject) jsonArrayLegs.get(b)).getJSONArray("steps");

                    // traversing all steps
                    for (int c = 0; c < jsonArraySteps.length(); c++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jsonArraySteps.get(c)).get("polyline")).get("points");
                        List<LatLng> list = decodePolyline(polyline);

                        // traversing all points
                        for (int d = 0; d < list.size(); d++) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("lat", Double.toString((list.get(d)).latitude));
                            hashMap.put("lng", Double.toString((list.get(d)).longitude));
                            path.add(hashMap);
                        }
                    }
                    routes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    // method to decode polyline points
    private List<LatLng> decodePolyline(String polylineEncoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0;
        int len = polylineEncoded.length();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = polylineEncoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polylineEncoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
