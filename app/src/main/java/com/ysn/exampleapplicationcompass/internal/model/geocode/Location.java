
package com.ysn.exampleapplicationcompass.internal.model.geocode;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Location {

    @SerializedName("lat")
    private Double mLat;
    @SerializedName("lng")
    private Double mLng;

    public Double getLat() {
        return mLat;
    }

    public Double getLng() {
        return mLng;
    }

    public static class Builder {

        private Double mLat;
        private Double mLng;

        public Location.Builder withLat(Double lat) {
            mLat = lat;
            return this;
        }

        public Location.Builder withLng(Double lng) {
            mLng = lng;
            return this;
        }

        public Location build() {
            Location Location = new Location();
            Location.mLat = mLat;
            Location.mLng = mLng;
            return Location;
        }

    }

}
