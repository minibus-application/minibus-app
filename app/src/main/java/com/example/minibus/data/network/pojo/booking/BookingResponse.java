package com.example.minibus.data.network.pojo.booking;

import com.example.minibus.data.network.pojo.city.BusStop;
import com.example.minibus.data.network.pojo.user.User;
import com.example.minibus.data.network.pojo.schedule.SelectedBusTrip;
import com.google.gson.annotations.SerializedName;

public class BookingResponse extends Booking {

    @SerializedName("user")
    private User user;

    @SerializedName("bookingsLeft")
    private int bookingsLeft;

    public BookingResponse(int id,
                           User user,
                           BusStop busStop,
                           SelectedBusTrip selectedBusTrip,
                           String departureDate,
                           Double price,
                           String status,
                           int bookingsLeft) {
        super(id, busStop, selectedBusTrip, departureDate, price, status);
        this.user = user;
        this.bookingsLeft = bookingsLeft;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getBookingsLeft() {
        return bookingsLeft;
    }

    public void setBookingsLeft(int bookingsLeft) {
        this.bookingsLeft = bookingsLeft;
    }
}
