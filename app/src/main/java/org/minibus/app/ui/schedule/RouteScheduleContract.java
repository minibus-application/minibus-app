package org.minibus.app.ui.schedule;

import androidx.annotation.StringRes;

import org.minibus.app.data.network.pojo.city.City;
import org.minibus.app.data.network.pojo.route.Route;
import org.minibus.app.data.network.pojo.schedule.RouteTrip;
import org.minibus.app.ui.base.Contract;

import java.time.LocalDate;
import java.util.List;

public interface RouteScheduleContract {

    interface View extends Contract.View {
        void showRouteDirection();
        void hideRouteDirection();
        void showJumpTopFab();
        void hideJumpTopFab();
        void showLoadingDataDialog();
        void hideLoadingDataDialog();
        void showRouteTripLoading();
        void hideRouteTripLoading();
        void showRefresh();
        void hideRefresh();
        void toggleRouteDirection();
        void disableRouteDirection();
        void enableRouteDirection();
        void setRouteScheduleData(List<RouteTrip> routeTrips, Route route);
        void setRouteDirectionDescription(String text);
        void setRouteDirectionDescription(@StringRes int resId);
        void setDepartureCity(String depCity);
        void setArrivalCity(String arrCity);
        void setRouteDirection(String depCity, String arrCity);
        void setOperationalDays(List<Integer> operationalDays);
        void setOptionsMenu(boolean isUserAuthorized);
        void openSortingOptions(RouteScheduleAdapter.SortingOption sortingOption);
        void openProfile();
        void openLogin();
        void openDepartureCities();
        void openArrivalCities();
        void openRouteTripSummary(RouteTrip routeTrip, Route route, LocalDate depDate);
        void openAbout();
        void jumpTop();
        void finish();
    }

    interface Presenter<V extends RouteScheduleContract.View> extends Contract.Presenter<V> {
        void onCreatedOptionsMenu();
        void onUserAuthorized();
        void onProfileMenuItemClick();
        void onLoginMenuItemClick();
        void onAboutMenuItemClick();
        void onLogoutMenuItemClick();
        void onBackPressed();
        void onStart(LocalDate depDate);
        void onRefresh(LocalDate depDate);
        void onRouteTripBooked(LocalDate depDate);
        void onRouteDirectionCollapsed();
        void onRouteDirectionExpanded();
        void onSwapRouteDirectionButtonClick(LocalDate depDate);
        void onCalendarDateClick(LocalDate depDate);
        void onArrivalCityChanged(City city, LocalDate depDate);
        void onDepartureCityChanged(City city, LocalDate depDate);
        void onDepartureCityFieldClick();
        void onArrivalCityFieldClick();
        void onSortByClick(RouteScheduleAdapter.SortingOption selectedSortingOption);
        void onRouteDirectionFabClick();
        void onJumpTopFabClick();
        void onRouteTripSelectButtonClick(LocalDate depDate, String itemId);
    }
}
