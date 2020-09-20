package org.minibus.app.ui.schedule;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import org.minibus.app.data.network.pojo.schedule.RouteTrip;

import java.util.List;

public class RouteScheduleDiffUtilCallback extends DiffUtil.Callback {

    private final List<RouteTrip> oldList;
    private final List<RouteTrip> newList;

    public RouteScheduleDiffUtilCallback(List<RouteTrip> oldList, List<RouteTrip> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        RouteTrip oldRouteTrip = oldList.get(oldItemPosition);
        RouteTrip newRouteTrip = newList.get(newItemPosition);
        return oldRouteTrip.getId().equals(newRouteTrip.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        RouteTrip oldRouteTrip = oldList.get(oldItemPosition);
        RouteTrip newRouteTrip = newList.get(newItemPosition);
        return oldRouteTrip.getId().equals(newRouteTrip.getId())
                && oldRouteTrip.getAvailableSeats() == newRouteTrip.getAvailableSeats()
                && oldRouteTrip.getDepartureTime().equals(newRouteTrip.getDepartureTime());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
