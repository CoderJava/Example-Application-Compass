
package com.ysn.exampleapplicationcompass.internal.model.geocode;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Northeast {

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

        public Northeast.Builder withLat(Double lat) {
            mLat = lat;
            return this;
        }

        public Northeast.Builder withLng(Double lng) {
            mLng = lng;
            return this;
        }

        public Northeast build() {
            Northeast Northeast = new Northeast();
            Northeast.mLat = mLat;
            Northeast.mLng = mLng;
            return Northeast;
        }

    }

}
