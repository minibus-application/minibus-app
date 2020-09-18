package org.minibus.app.ui.schedule.trip;

import org.minibus.app.ui.base.Contract;

public interface RouteTripContract {

    interface View extends Contract.View {

        void setSeatsCounterRange(int minSeatsValue, int maxSeatsValue);
        void setPassengerName(String name);
        void disableConfirmReservationButton();
        void enableConfirmReservationButton();
        void closeOnBooked();
        void close();
    }

    interface Presenter<V extends RouteTripContract.View> extends Contract.Presenter<V> {

        void onStart(int availableSeats);
        void onConfirmReservationButtonClick(String depDate, String busTripId, int seatsToReserve);
        void onSeatsCountChanged(int newValue);
    }
}