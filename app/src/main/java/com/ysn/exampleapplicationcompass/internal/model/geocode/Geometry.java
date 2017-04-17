
package com.ysn.exampleapplicationcompass.internal.model.geocode;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Geometry {

    @SerializedName("bounds")
    private Bounds mBounds;
    @SerializedName("location")
    private Location mLocation;
    @SerializedName("location_type")
    private String mLocationType;
    @SerializedName("viewport")
    private Viewport mViewport;

    public Bounds getBounds() {
        return mBounds;
    }

    public Location getLocation() {
        return mLocation;
    }

    public String getLocationType() {
        return mLocationType;
    }

    public Viewport getViewport() {
        return mViewport;
    }

    public static class Builder {

        private Bounds mBounds;
        private Location mLocation;
        private String mLocationType;
        private Viewport mViewport;

        public Geometry.Builder withBounds(Bounds bounds) {
            mBounds = bounds;
            return this;
        }

        public Geometry.Builder withLocation(Location location) {
            mLocation = location;
            return this;
        }

        public Geometry.Builder withLocationType(String locationType) {
            mLocationType = locationType;
            return this;
        }

        public Geometry.Builder withViewport(Viewport viewport) {
            mViewport = viewport;
            return this;
        }

        public Geometry build() {
            Geometry Geometry = new Geometry();
            Geometry.mBounds = mBounds;
            Geometry.mLocation = mLocation;
            Geometry.mLocationType = mLocationType;
            Geometry.mViewport = mViewport;
            return Geometry;
        }

    }

}
