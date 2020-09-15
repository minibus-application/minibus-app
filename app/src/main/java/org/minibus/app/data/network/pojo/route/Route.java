package org.minibus.app.data.network.pojo.route;

import com.google.gson.annotations.SerializedName;

import org.minibus.app.data.network.pojo.city.City;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public class Route implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("desc")
    private String description;

    @SerializedName("from")
    private City departureCity;

    @SerializedName("to")
    private City arrivalCity;

    @SerializedName("opDays")
    private List<Integer> operationalDays;

    public Route(String id,
                 String description,
                 City departureCity,
                 City arrivalCity,
                 List<Integer> operationalDays) {
        this.id = id;
        this.description = description;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.operationalDays = operationalDays;
    }

    public long getIdLong() {
        return new BigInteger(id, 16).longValue();
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public City getDepartureCity() {
        return departureCity;
    }

    public City getArrivalCity() {
        return arrivalCity;
    }

    public List<Integer> getOperationalDays() {
        return operationalDays;
    }
}
