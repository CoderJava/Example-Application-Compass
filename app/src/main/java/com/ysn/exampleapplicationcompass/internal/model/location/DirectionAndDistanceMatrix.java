package com.ysn.exampleapplicationcompass.internal.model.location;

import com.google.gson.JsonObject;

import org.json.JSONObject;

/**
 * Created by root on 21/04/17.
 */

public class DirectionAndDistanceMatrix {
    private JsonObject jsonObjectDirection;
    private JsonObject jsonObjectDistanceMatrix;

    public DirectionAndDistanceMatrix(JsonObject jsonObjectDirection, JsonObject jsonObjectDistanceMatrix) {
        this.jsonObjectDirection = jsonObjectDirection;
        this.jsonObjectDistanceMatrix = jsonObjectDistanceMatrix;
    }

    public JsonObject getJsonObjectDirection() {
        return jsonObjectDirection;
    }

    public void setJsonObjectDirection(JsonObject jsonObjectDirection) {
        this.jsonObjectDirection = jsonObjectDirection;
    }

    public JsonObject getJsonObjectDistanceMatrix() {
        return jsonObjectDistanceMatrix;
    }

    public void setJsonObjectDistanceMatrix(JsonObject jsonObjectDistanceMatrix) {
        this.jsonObjectDistanceMatrix = jsonObjectDistanceMatrix;
    }
}
