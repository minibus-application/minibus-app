package org.minibus.app.ui.schedule;

import androidx.annotation.StringRes;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.schedule.BusScheduleResponse;
import org.minibus.app.data.network.pojo.schedule.BusTrip;
import org.minibus.app.ui.base.Contract;

import java.util.List;

public interface BusScheduleContract {

    interface View extends Contract.View {

        void showFilter();
        void showSwapDirectionAnimation();
        void showJumpTopFab();
        void showLoadingDataDialog();
        void hideLoadingDataDialog();
        void showRefresh();
        void showBusTripLoading();
        void hideBusTripLoading();
        void hideRefresh();
        void hideJumpTopFab();
        void toggleFilter();
        void updateProfileBadge();
        void setProfileBadge(int bookingsCount);
        void setBusScheduleData(List<BusTrip> busTrips, Route route);
        void setDirectionDescription(String text);
        void setDirectionDescription(@StringRes int resId);
        void setDepartureCity(String depCity);
        void setArrivalCity(String arrCity);
        void setDirection(String depCity, String arrCity);
        void setOperationalDays(List<Integer> daysOfWeek);
        void openProfile();
        void openLogin();
        void openDepartureCities();
        void openArrivalCities();
        void openBusTripSummary(BusTrip busTrip, Route route, String depDate);
        void jumpTop();
        void finish();
    }

    interface Presenter<V extends BusScheduleContract.View> extends Contract.Presenter<V> {

        void onBackPressed();
        void onStart(String depDate);
        void onCreateProfileBadge();
        void onRefresh(String depDate);
        void onUserLoggedIn();
        void onUserLoggedOut();
        void onUserBookedBusTrip(String depDate);
        void onUserBookingsUpdate();
        void onFilterCollapsed();
        void onFilterExpanded();
        void onDirectionSwapButtonClick(String depDate);
        void onDateClick(String depDate);
        void onArrivalCityChange(City city, String depDate);
        void onDepartureCityChange(City city, String depDate);
        void onDepartureCityClick();
        void onArrivalCityClick();
        void onProfileIconClick();
        void onFilterFabClick();
        void onJumpTopFabClick();
        void onBusTripSelectButtonClick(String depDate, long id, int pos, String routeId);
    }
}
