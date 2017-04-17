
package com.ysn.exampleapplicationcompass.internal.model.geocode;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Southwest {

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

        public Southwest.Builder withLat(Double lat) {
            mLat = lat;
            return this;
        }

        public Southwest.Builder withLng(Double lng) {
            mLng = lng;
            return this;
        }

        public Southwest build() {
            Southwest Southwest = new Southwest();
            Southwest.mLat = mLat;
            Southwest.mLng = mLng;
            return Southwest;
        }

    }

}
