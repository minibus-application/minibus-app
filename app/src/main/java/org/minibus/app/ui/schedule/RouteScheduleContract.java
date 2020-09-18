package org.minibus.app.ui.schedule;

import androidx.annotation.StringRes;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.ui.base.Contract;

import java.util.List;

public interface RouteScheduleContract {

    interface View extends Contract.View {

        void showFilter();
        void showSwapDirectionAnimation();
        void showJumpTopFab();
        void showLoadingDataDialog();
        void hideLoadingDataDialog();
        void showRefresh();
        void showRouteTripLoading();
        void hideRouteTripLoading();
        void hideRefresh();
        void hideJumpTopFab();
        void toggleFilter();
        void setRouteScheduleData(List<RouteTrip> routeTrips, Route route);
        void setDirectionDescription(String text);
        void setDirectionDescription(@StringRes int resId);
        void setDepartureCity(String depCity);
        void setArrivalCity(String arrCity);
        void setDirection(String depCity, String arrCity);
        void setOperationalDays(List<Integer> daysOfWeek);
        void openSortingOptions(RouteScheduleAdapter.SortingOption sortingOption);
        void openProfile();
        void openLogin();
        void openDepartureCities();
        void openArrivalCities();
        void openBusTripSummary(RouteTrip routeTrip, Route route, String depDate);
        void jumpTop();
        void finish();
    }

    interface Presenter<V extends RouteScheduleContract.View> extends Contract.Presenter<V> {

        void onBackPressed();
        void onStart(String depDate);
        void onRefresh(String depDate);
        void onUserBookedBusTrip(String depDate);
        void onFilterCollapsed();
        void onFilterExpanded();
        void onDirectionSwapButtonClick(String depDate);
        void onDateClick(String depDate);
        void onArrivalCityChange(City city, String depDate);
        void onDepartureCityChange(City city, String depDate);
        void onDepartureCityClick();
        void onArrivalCityClick();
        void onProfileIconClick();
        void onSortByItemClick(RouteScheduleAdapter.SortingOption selectedSortingOption);
        void onRouteFabClick();
        void onJumpTopFabClick();
        void onRouteTripSelectButtonClick(String depDate, String itemId, String routeId);
    }
}
