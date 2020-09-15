package org.minibus.app.data.network.pojo.carrier;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigInteger;

public class Carrier implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private String rating;

    @SerializedName("tripCostFactor")
    private String costFactor;

    public Carrier(String id, String name, String rating, String costFactor) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.costFactor = costFactor;
    }

    public long getId() {
        return new BigInteger(id, 16).longValue();
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getCostFactor() {
        return costFactor;
    }
}
