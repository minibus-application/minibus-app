package org.minibus.app.data.network.pojo.booking;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.schedule.BusTrip;
import org.minibus.app.data.network.pojo.user.User;
import com.google.gson.annotations.SerializedName;

public class BookingRequest {

    @SerializedName("user")
    private User user;

    @SerializedName("stop")
    private City city;

    @SerializedName("selectedSchedule")
    private BusTrip selectedBusTrip;

    @SerializedName("passengerQuantity")
    private int passengersCount;

    @SerializedName("departureDate")
    private String departureDate;

    public BookingRequest(User user,
                          City city,
                          BusTrip selectedBusTrip,
                          int passengersCount,
                          String departureDate) {
        this.user = user;
        this.city = city;
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
