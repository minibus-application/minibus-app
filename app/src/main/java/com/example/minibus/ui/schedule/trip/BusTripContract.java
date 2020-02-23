package com.example.minibus.ui.schedule.trip;

import com.example.minibus.data.network.pojo.city.BusStop;
import com.example.minibus.data.network.pojo.schedule.BusTrip;
import com.example.minibus.ui.base.Contract;

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
