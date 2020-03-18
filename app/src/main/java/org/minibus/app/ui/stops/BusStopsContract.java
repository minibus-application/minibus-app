package org.minibus.app.ui.stops;

import org.minibus.app.data.network.pojo.city.CityResponse;
import org.minibus.app.data.network.pojo.city.BusStop;
import org.minibus.app.ui.base.Contract;

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
