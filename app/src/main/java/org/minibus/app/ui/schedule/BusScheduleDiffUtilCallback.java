package org.minibus.app.ui.schedule;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import org.minibus.app.data.network.pojo.schedule.BusTrip;

import java.util.List;

public class BusScheduleDiffUtilCallback extends DiffUtil.Callback {

    private final List<BusTrip> oldList;
    private final List<BusTrip> newList;

    public BusScheduleDiffUtilCallback(List<BusTrip> oldList, List<BusTrip> newList) {
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
        BusTrip oldBusTrip = oldList.get(oldItemPosition);
        BusTrip newBusTrip = newList.get(newItemPosition);
        return oldBusTrip.getId() == newBusTrip.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BusTrip oldBusTrip = oldList.get(oldItemPosition);
        BusTrip newBusTrip = newList.get(newItemPosition);
        return oldBusTrip.getId() == newBusTrip.getId()
                && oldBusTrip.getSeatsAvailable() == newBusTrip.getSeatsAvailable()
                && oldBusTrip.getDepartureTime().equals(newBusTrip.getDepartureTime());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
