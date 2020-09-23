package org.minibus.app.ui.profile;

import org.minibus.app.data.network.pojo.booking.Booking;
import org.minibus.app.ui.base.Contract;

import java.time.LocalDate;
import java.util.List;

public interface UserProfileContract {

    interface View extends Contract.View {

        void setUserBookingsData(List<Booking> bookings);
        void setUserData(String userName, String userPhone);
        void setCheckedTabCounter(int bookingsCount);
        void resetTabsCounter();
        void hideRefresh();
        void logout();
        void close();
    }

    interface Presenter<V extends UserProfileContract.View> extends Contract.Presenter<V> {

        void onStart(UserProfileFragment.BookingsTab checkedTab);
        void onBookingsTabChecked(UserProfileFragment.BookingsTab checkedTab);
        void onBookingsTabRefresh(UserProfileFragment.BookingsTab checkedTab);
        void onBookingCancelButtonClick(String bookingId);
        void onRouteScheduleButtonClick();
        void onLogoutButtonClick();
        void onCloseButtonClick();
    }
}
