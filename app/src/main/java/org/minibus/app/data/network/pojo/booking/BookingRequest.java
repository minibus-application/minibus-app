package org.minibus.app.data.network.pojo.booking;

import org.minibus.app.data.network.pojo.city.BusStop;
import org.minibus.app.data.network.pojo.schedule.SelectedBusTrip;
import org.minibus.app.data.network.pojo.user.User;
import com.google.gson.annotations.SerializedName;

public class BookingRequest {

    @SerializedName("user")
    private User user;

    @SerializedName("stop")
    private BusStop busStop;

    @SerializedName("selectedSchedule")
    private SelectedBusTrip selectedBusTrip;

    @SerializedName("passengerQuantity")
    private int passengersCount;

    @SerializedName("departureDate")
    private String departureDate;

    public BookingRequest(User user,
                          BusStop busStop,
                          SelectedBusTrip selectedBusTrip,
                          int passengersCount,
                          String departureDate) {
        this.user = user;
        this.busStop = busStop;
        this.selectedBusTrip = selectedBusTrip;
        this.passengersCount = passengersCount;
        this.departureDate = departureDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public int getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(int passengersCount) {
        this.passengersCount = passengersCount;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
}
