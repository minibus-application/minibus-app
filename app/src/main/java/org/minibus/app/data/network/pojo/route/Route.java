package org.minibus.app.data.network.pojo.route;

import com.google.gson.annotations.SerializedName;

import org.minibus.app.data.network.pojo.city.City;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return id.equals(route.id) &&
                Objects.equals(departureCity, route.departureCity) &&
                Objects.equals(arrivalCity, route.arrivalCity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departureCity, arrivalCity);
    }
}
