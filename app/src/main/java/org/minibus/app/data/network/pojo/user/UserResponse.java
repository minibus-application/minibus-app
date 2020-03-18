package org.minibus.app.data.network.pojo.user;

import org.minibus.app.data.network.pojo.booking.Booking;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserResponse extends User implements Serializable {

    @SerializedName("bookings")
    private List<Booking> bookings;

    @SerializedName("totalBookingLimit")
    private int bookingsLimit;

    public UserResponse(int id,
                        String name,
                        String phone,
                        List<Booking> bookings,
                        int bookingsLimit) {
        super(id, name, phone);
        this.bookings = bookings;
        this.bookingsLimit = bookingsLimit;
    }

    public User getUser() {
        return this;
    }

    public boolean isBookingsListEmpty() {
        return getBookingsCount() == 0;
    }

    public int getBookingsCount() {
        return bookings == null ? 0 : bookings.size();
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public int getBookingsLimit() {
        return bookingsLimit;
    }

    public void setBookingsLimit(int bookingsLimit) {
        this.bookingsLimit = bookingsLimit;
    }
}
