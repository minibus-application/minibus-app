package org.minibus.app.data.network.pojo.user;

import org.minibus.app.data.network.pojo.booking.Booking;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserResponse implements Serializable {

    @SerializedName("user")
    private User user;

    @SerializedName("bookings")
    private List<Booking> bookings;

    @SerializedName("activeBookings")
    private int activeBookings;

    @SerializedName("totalBookings")
    private int totalBookings;

    @SerializedName("token")
    private String token;

    public UserResponse(User user, List<Booking> bookings, int activeBookings, int totalBookings) {
        this.user = user;
        this.bookings = bookings;
        this.activeBookings = activeBookings;
        this.totalBookings = totalBookings;
    }

    public User getUser() {
        return user;
    }

    public boolean isBookingsListEmpty() {
        return bookings == null || bookings.size() == 0;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public int getActiveBookings() {
        return activeBookings;
    }

    public int getTotalBookings() {
        return totalBookings;
    }

    public String getToken() {
        return token;
    }
}
