package org.minibus.app.data.network.pojo.booking;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.schedule.BusTrip;

import com.google.gson.annotations.SerializedName;

public class Booking {

    @SerializedName("id")
    private int id;

    @SerializedName("stop")
    private City city;

    @SerializedName("selectedSchedule")
    private BusTrip selectedBusTrip;

    @SerializedName("departureDate")
    private String departureDate;

    @SerializedName("price")
    private Double price;

    @SerializedName("status")
    private String status;

    public Booking(int id,
                   City city,
                   BusTrip selectedBusTrip,
                   String departureDate,
                   Double price,
                   String status) {
        this.id = id;
        this.city = city;
        this.selectedBusTrip = selectedBusTrip;
        this.departureDate = departureDate;
        this.price = price;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public BusTrip getSelectedBusTrip() {
        return selectedBusTrip;
    }

    public void setSelectedBusTrip(BusTrip selectedBusTrip) {
        this.selectedBusTrip = selectedBusTrip;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
