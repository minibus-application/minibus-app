package org.minibus.app.data.network.pojo.schedule;

import com.google.gson.annotations.SerializedName;

import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.vehicle.Vehicle;

import java.io.Serializable;
import java.math.BigInteger;

public class RouteTrip implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("cost")
    private String cost;

    @SerializedName("currency")
    private String currency;

    @SerializedName("fromTime")
    private String departureTime;

    @SerializedName("toTime")
    private String arrivalTime;

    @SerializedName("duration")
    private String duration;

    @SerializedName("vehicle")
    private Vehicle vehicle;

    @SerializedName("seatsBooked")
    private int seatsBooked;

    private Route route;

    public RouteTrip(String cost,
                     String currency,
                     String departureTime,
                     String arrivalTime,
                     String duration,
                     Vehicle vehicle,
                     Route route,
                     int seatsBooked) {
        this.cost = cost;
        this.currency = currency;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.vehicle = vehicle;
        this.route = route;
        this.seatsBooked = seatsBooked;
    }

    public long getLongId() {
        return new BigInteger(id, 16).longValue();
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getId() {
        return id;
    }

    public String getCost() {
        return cost;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Route getRoute() {
        return route;
    }

    public int getAvailableSeats() {
        return vehicle.getCapacity() - seatsBooked;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }
}
