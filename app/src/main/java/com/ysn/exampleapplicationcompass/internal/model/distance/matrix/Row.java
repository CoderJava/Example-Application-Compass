
package com.ysn.exampleapplicationcompass.internal.model.distance.matrix;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Row {

    @SerializedName("elements")
    private List<Element> mElements;

    public List<Element> getElements() {
        return mElements;
    }

    public void setElements(List<Element> elements) {
        mElements = elements;
    }

}
