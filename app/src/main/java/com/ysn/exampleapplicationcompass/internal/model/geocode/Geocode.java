
package com.ysn.exampleapplicationcompass.internal.model.geocode;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Geocode {

    @SerializedName("results")
    private List<Result> mResults;
    @SerializedName("status")
    private String mStatus;

    public List<Result> getResults() {
        return mResults;
    }

    public String getStatus() {
        return mStatus;
    }

    public static class Builder {

        private List<Result> mResults;
        private String mStatus;

        public Geocode.Builder withResults(List<Result> results) {
            mResults = results;
            return this;
        }

        public Geocode.Builder withStatus(String status) {
            mStatus = status;
            return this;
        }

        public Geocode build() {
            Geocode Geocode = new Geocode();
            Geocode.mResults = mResults;
            Geocode.mStatus = mStatus;
            return Geocode;
        }

    }

}
