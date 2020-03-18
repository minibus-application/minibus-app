package org.minibus.app.ui.schedule.trip;

import org.minibus.app.data.network.pojo.city.BusStop;
import org.minibus.app.data.network.pojo.schedule.BusTrip;
import org.minibus.app.ui.base.Contract;

public interface BusTripContract {

    interface View extends Contract.View {

        void setPassengersCount(int passengersCount);
        void openLogin();
        void closeOnBooked();
        void close();
        void hide();
        void resume();
    }

    interface Presenter<V extends BusTripContract.View> extends Contract.Presenter<V> {

        void onBookClick(String departureDate, BusTrip busTrip, BusStop departureBusStop, int seatsCount);
        void onSetupPassengersOptions(int seatsCount);
        void onCancelClick();
    }
}
