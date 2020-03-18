package org.minibus.app.data.network.pojo.schedule;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BusScheduleResponse implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String stopName;

    @SerializedName("departureCityName")
    private String departureCityName;

    @SerializedName("arrivalCityName")
    private String arrivalCityName;

    @SerializedName("schedule")
    private List<BusTrip> busTrips;

    public BusScheduleResponse(int id,
                               String stopName,
                               String departureCityName,
                               String arrivalCityName,
                               List<BusTrip> busTrips) {
        this.id = id;
        this.stopName = stopName;
        this.departureCityName = departureCityName;
        this.arrivalCityName = arrivalCityName;
        this.busTrips = busTrips;
    }

    public List<BusTrip> getBusTrips() {
        return busTrips;
    }

    public void setBusTrips(List<BusTrip> busTrips) {
        this.busTrips = busTrips;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getDepartureCityName() {
        return departureCityName;
    }

    public void setDepartureCityName(String departureCityName) {
        this.departureCityName = departureCityName;
    }

    public String getArrivalCityName() {
        return arrivalCityName;
    }

    public void setArrivalCityName(String arrivalCityName) {
        this.arrivalCityName = arrivalCityName;
    }

    public boolean isBusTripsListEmpty() {
        return getBusTripsCount() == 0;
    }

    public int getBusTripsCount() {
        return busTrips == null ? 0 : busTrips.size();
    }

    public Optional<BusTrip> getBusTripById(int id) {
        return busTrips.stream().filter(busTrip -> busTrip.getId() == id).findFirst();
    }
}
