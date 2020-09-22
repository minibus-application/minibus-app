package org.minibus.app.ui.schedule.trip;

import org.minibus.app.ui.base.Contract;

import java.time.LocalDate;

public interface RouteTripContract {

    interface View extends Contract.View {

        void setPassengerName(String name);
        void setSeatsCount(int seatsCount);
        void closeOnBooked();
        void close();
    }

    interface Presenter<V extends RouteTripContract.View> extends Contract.Presenter<V> {

        void onStart(int availableSeats);
        void onConfirmReservationButtonClick(LocalDate depDate, String tripId, String routeId, int seatsCount);
    }
}
