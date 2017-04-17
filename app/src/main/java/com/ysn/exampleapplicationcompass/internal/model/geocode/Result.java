
package com.ysn.exampleapplicationcompass.internal.model.geocode;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Result {

    @SerializedName("address_components")
    private List<AddressComponent> mAddressComponents;
    @SerializedName("formatted_address")
    private String mFormattedAddress;
    @SerializedName("geometry")
    private Geometry mGeometry;
    @SerializedName("place_id")
    private String mPlaceId;
    @SerializedName("types")
    private List<String> mTypes;

    public List<AddressComponent> getAddressComponents() {
        return mAddressComponents;
    }

    public String getFormattedAddress() {
        return mFormattedAddress;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public List<String> getTypes() {
        return mTypes;
    }

    public static class Builder {

        private List<AddressComponent> mAddressComponents;
        private String mFormattedAddress;
        private Geometry mGeometry;
        private String mPlaceId;
        private List<String> mTypes;

        public Result.Builder withAddressComponents(List<AddressComponent> addressComponents) {
            mAddressComponents = addressComponents;
            return this;
        }

        public Result.Builder withFormattedAddress(String formattedAddress) {
            mFormattedAddress = formattedAddress;
            return this;
        }

        public Result.Builder withGeometry(Geometry geometry) {
            mGeometry = geometry;
            return this;
        }

        public Result.Builder withPlaceId(String placeId) {
            mPlaceId = placeId;
            return this;
        }

        public Result.Builder withTypes(List<String> types) {
            mTypes = types;
            return this;
        }

        public Result build() {
            Result Result = new Result();
            Result.mAddressComponents = mAddressComponents;
            Result.mFormattedAddress = mFormattedAddress;
            Result.mGeometry = mGeometry;
            Result.mPlaceId = mPlaceId;
            Result.mTypes = mTypes;
            return Result;
        }

    }

}
