package org.minibus.app.data.network.pojo.booking;

import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.data.network.pojo.user.User;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigInteger;

public class Booking implements Serializable {

    @SerializedName("_id")
    private String id;

    @SerializedName("trip")
    private RouteTrip routeTrip;

    @SerializedName("user")
    private User user;

    @SerializedName("tripDate")
    private String departureDate;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("enRoute")
    private boolean isEnRoute;

    @SerializedName("active")
    private boolean isActive;

    public Booking(String id,
                   RouteTrip routeTrip,
                   User user,
                   String departureDate,
                   String startDate,
                   String endDate,
                   boolean isEnRoute,
                   boolean isActive) {
        this.id = id;
        this.routeTrip = routeTrip;
        this.user = user;
        this.departureDate = departureDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isEnRoute = isEnRoute;
        this.isActive = isActive;
    }

    public long getLongId() {
        return new BigInteger(id, 16).longValue();
    }

    public String getId() {
        return id;
    }

    public RouteTrip getRouteTrip() {
        return routeTrip;
    }

    public User getUser() {
        return user;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isEnRoute() {
        return isEnRoute;
    }

    public boolean isActive() {
        return isActive;
    }
}
