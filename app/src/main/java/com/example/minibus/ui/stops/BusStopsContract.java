package com.example.minibus.ui.stops;

import com.example.minibus.data.network.pojo.city.CityResponse;
import com.example.minibus.data.network.pojo.city.BusStop;
import com.example.minibus.ui.base.Contract;

import java.util.List;

public interface BusStopsContract {

    interface View extends Contract.View {

        void setCitiesData(List<CityResponse> cities, int prevSelectedBusStopId);
        void changeDepartureBusStop(BusStop selectedBusStop);
        void changeArrivalBusStop(BusStop finalArrivalBusStop, BusStop startDepartureBusStop);
        void close();
    }

    interface Presenter<V extends BusStopsContract.View> extends Contract.Presenter<V> {

        void onStart();
        void onRefreshButtonClick();
        void onCloseButtonClick();
        void onDepartureBusStopSelected(BusStop selectedBusStop);
    }
}
