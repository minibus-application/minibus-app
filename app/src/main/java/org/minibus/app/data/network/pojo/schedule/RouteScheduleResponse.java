package org.minibus.app.data.network.pojo.schedule;

import com.google.gson.annotations.SerializedName;

import org.minibus.app.data.network.pojo.route.Route;

import java.io.Serializable;
import java.util.List;

public class RouteScheduleResponse implements Serializable {

    @SerializedName("timeline")
    private List<RouteTrip> routeTrips;

    @SerializedName("route")
    private Route route;

    public RouteScheduleResponse(List<RouteTrip> routeTrips, Route route) {
        this.routeTrips = routeTrips;
        this.route = route;
    }

    public List<RouteTrip> getRouteTrips() {
        routeTrips.forEach(routeTrip -> routeTrip.setRoute(getRoute()));
        return routeTrips;
    }

    public Route getRoute() {
        return route;
    }
}
