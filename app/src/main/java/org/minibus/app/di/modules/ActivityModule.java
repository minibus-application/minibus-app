package org.minibus.app.di.modules;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.minibus.app.data.network.model.BookingModel;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.model.BusScheduleModel;
import org.minibus.app.data.network.model.UserModel;
import org.minibus.app.di.ActivityContext;
import org.minibus.app.ui.login.LoginContract;
import org.minibus.app.ui.login.LoginPresenter;
import org.minibus.app.ui.profile.UserProfileContract;
import org.minibus.app.ui.profile.UserProfilePresenter;
import org.minibus.app.ui.schedule.BusScheduleContract;
import org.minibus.app.ui.schedule.BusSchedulePresenter;
import org.minibus.app.ui.schedule.trip.BusTripContract;
import org.minibus.app.ui.schedule.trip.BusTripPresenter;
import org.minibus.app.ui.stops.BusStopsContract;
import org.minibus.app.ui.stops.BusStopsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return activity;
    }

    @Provides
    BusScheduleContract.Presenter<BusScheduleContract.View> provideBusSchedulePresenter
            (BusSchedulePresenter<BusScheduleContract.View> presenter) {
        return presenter;
    }

    @Provides
    BusStopsContract.Presenter<BusStopsContract.View> provideCityStopsPresenter
            (BusStopsPresenter<BusStopsContract.View> presenter) {
        return presenter;
    }

    @Provides
    BusTripContract.Presenter<BusTripContract.View> provideBusTripPresenter
            (BusTripPresenter<BusTripContract.View> presenter) {
        return presenter;
    }

    @Provides
    UserProfileContract.Presenter<UserProfileContract.View> provideUserProfilePresenter
            (UserProfilePresenter<UserProfileContract.View> presenter) {
        return presenter;
    }

    @Provides
    LoginContract.Presenter<LoginContract.View> provideLoginPresenter
            (LoginPresenter<LoginContract.View> presenter) {
        return presenter;
    }

    @Provides
    BusScheduleModel provideBusScheduleModel() {
        return new BusScheduleModel();
    }

    @Provides
    CitiesModel provideCitiesModel() {
        return new CitiesModel();
    }

    @Provides
    BookingModel provideBookingModel() {
        return new BookingModel();
    }

    @Provides
    UserModel provideUserModel() {
        return new UserModel();
    }
}
