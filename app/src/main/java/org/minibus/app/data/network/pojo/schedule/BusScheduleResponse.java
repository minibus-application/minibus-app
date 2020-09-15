package org.minibus.app.data.network.pojo.schedule;

import org.minibus.app.data.network.pojo.BaseResponse;
import org.minibus.app.data.network.pojo.route.Route;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class BusScheduleResponse extends BaseResponse<List<BusTrip>> implements Serializable {

    public BusScheduleResponse(List<BusTrip> result) {
        super(result);
    }

    public Route getRoute() {
        return getResult().get(0).getRoute();
    }

    public List<BusTrip> getBusTrips() {
        return getResult();
    }

    public boolean isEmpty() {
        return getBusTrips() == null || getBusTrips().isEmpty();
    }

    public Optional<BusTrip> getBusTripById(long id) {
        return getBusTrips().stream().filter(busTrip -> busTrip.getId() == id).findFirst();
    }
}
