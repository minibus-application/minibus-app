package org.minibus.app.data.network.pojo.carrier;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

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

    public String getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrier carrier = (Carrier) o;
        return id.equals(carrier.id) &&
                Objects.equals(name, carrier.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
