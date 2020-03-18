package org.minibus.app.data.network.pojo.booking;

import org.minibus.app.data.network.pojo.city.BusStop;
import org.minibus.app.data.network.pojo.schedule.SelectedBusTrip;
import com.google.gson.annotations.SerializedName;

public class Booking {

    @SerializedName("id")
    private int id;

    @SerializedName("stop")
    private BusStop busStop;

    @SerializedName("selectedSchedule")
    private SelectedBusTrip selectedBusTrip;

    @SerializedName("departureDate")
    private String departureDate;

    @SerializedName("price")
    private Double price;

    @SerializedName("status")
    private String status;

    public Booking(int id,
                   BusStop busStop,
                   SelectedBusTrip selectedBusTrip,
                   String departureDate,
                   Double price,
                   String status) {
        this.id = id;
        this.busStop = busStop;
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

    public BusStop getBusStop() {
        return busStop;
    }

    public void setBusStop(BusStop busStop) {
        this.busStop = busStop;
    }

    public SelectedBusTrip getSelectedBusTrip() {
        return selectedBusTrip;
    }

    public void setSelectedBusTrip(SelectedBusTrip selectedBusTrip) {
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
