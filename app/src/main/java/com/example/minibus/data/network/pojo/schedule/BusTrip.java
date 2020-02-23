package com.example.minibus.data.network.pojo.schedule;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BusTrip extends SelectedBusTrip implements Serializable {

    @SerializedName("availableSeats")
    private int seatsCount;

    public BusTrip(int id,
                   String departureTime,
                   String arrivalTime,
                   int seatsCount) {
        super(id, departureTime, arrivalTime);
        this.seatsCount = seatsCount;
    }

    public SelectedBusTrip getSelected() {
        return new SelectedBusTrip(getId(), getDepartureTime(), getArrivalTime());
    }

    public int getSeatsCount() {
        return seatsCount;
    }

    public void setSeatsCount(int seatsCount) {
        this.seatsCount = seatsCount;
    }

    public String getDepartureHours() {
        return getDepartureTime().split(":")[0];
    }
}
