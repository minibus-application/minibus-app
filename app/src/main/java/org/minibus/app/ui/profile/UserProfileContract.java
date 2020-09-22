package org.minibus.app.ui.profile;

import org.minibus.app.data.network.pojo.booking.Booking;
import org.minibus.app.ui.base.Contract;

import java.time.LocalDate;
import java.util.List;

public interface UserProfileContract {

    interface View extends Contract.View {

        void setUserBookingsData(List<Booking> bookings);
        void setUserData(String userName, String userPhone);
        void setActiveTabCounter(int bookingsCount);
        void hideRefresh();
        void logout();
        void close();
    }

    interface Presenter<V extends UserProfileContract.View> extends Contract.Presenter<V> {

        void onStart();
        void onLogoutButtonClick();
        void onCloseButtonClick();
        void onActiveBookingsTabSelected();
        void onBookingsHistoryTabSelected();
        void onRefreshActiveBookings();
        void onRefreshBookingsHistory();
        void onBookingCancelButtonClick(String bookingId);
        void onRouteScheduleButtonClick();
    }
}
