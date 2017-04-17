
package com.ysn.exampleapplicationcompass.internal.model.geocode;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Viewport {

    @SerializedName("northeast")
    private Northeast mNortheast;
    @SerializedName("southwest")
    private Southwest mSouthwest;

    public Northeast getNortheast() {
        return mNortheast;
    }

    public Southwest getSouthwest() {
        return mSouthwest;
    }

    public static class Builder {

        private Northeast mNortheast;
        private Southwest mSouthwest;

        public Viewport.Builder withNortheast(Northeast northeast) {
            mNortheast = northeast;
            return this;
        }

        public Viewport.Builder withSouthwest(Southwest southwest) {
            mSouthwest = southwest;
            return this;
        }

        public Viewport build() {
            Viewport Viewport = new Viewport();
            Viewport.mNortheast = mNortheast;
            Viewport.mSouthwest = mSouthwest;
            return Viewport;
        }

    }

}
