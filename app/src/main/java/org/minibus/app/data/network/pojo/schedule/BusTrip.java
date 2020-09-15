package org.minibus.app.data.network.pojo.schedule;

import com.google.gson.annotations.SerializedName;

import org.minibus.app.data.network.pojo.BaseResponse;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.transport.Transport;

import java.io.Serializable;
import java.math.BigInteger;

public class BusTrip implements Serializable {

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

    @SerializedName("van")
    private Transport transport;

    @SerializedName("route")
    private Route route;

    @SerializedName("seatsBooked")
    private int seatsBooked;

    public BusTrip(String cost,
                   String currency,
                   String departureTime,
                   String arrivalTime,
                   String duration,
                   Transport transport,
                   Route route,
                   int seatsBooked) {
        this.cost = cost;
        this.currency = currency;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.transport = transport;
        this.route = route;
        this.seatsBooked = seatsBooked;
    }

    public long getId() {
        return new BigInteger(id, 16).longValue();
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

    public Transport getTransport() {
        return transport;
    }

    public Route getRoute() {
        return route;
    }

    public int getSeatsAvailable() {
        return transport.getCapacity() - seatsBooked;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }
}
