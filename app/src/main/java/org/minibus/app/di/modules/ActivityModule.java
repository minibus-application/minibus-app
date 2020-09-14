package org.minibus.app.di.modules;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.model.BookingModel;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.model.BusScheduleModel;
import org.minibus.app.data.network.model.UserModel;
import org.minibus.app.di.ActivityContext;
import org.minibus.app.ui.cities.arrival.ArrivalCitiesContract;
import org.minibus.app.ui.cities.arrival.ArrivalCitiesPresenter;
import org.minibus.app.ui.cities.departure.DepartureCitiesContract;
import org.minibus.app.ui.cities.departure.DepartureCitiesPresenter;
import org.minibus.app.ui.login.LoginContract;
import org.minibus.app.ui.login.LoginPresenter;
import org.minibus.app.ui.profile.UserProfileContract;
import org.minibus.app.ui.profile.UserProfilePresenter;
import org.minibus.app.ui.schedule.BusScheduleContract;
import org.minibus.app.ui.schedule.BusSchedulePresenter;
import org.minibus.app.ui.schedule.trip.BusTripContract;
import org.minibus.app.ui.schedule.trip.BusTripPresenter;
import org.minibus.app.ui.cities.CitiesContract;
import org.minibus.app.ui.cities.CitiesPresenter;

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
    CitiesContract.Presenter<CitiesContract.View> provideCitiesPresenter
            (CitiesPresenter<CitiesContract.View> presenter) {
        return presenter;
    }

    @Provides
    DepartureCitiesContract.Presenter<DepartureCitiesContract.View> provideDepartureCitiesPresenter
            (DepartureCitiesPresenter<DepartureCitiesContract.View> presenter) {
        return presenter;
    }

    @Provides
    ArrivalCitiesContract.Presenter<ArrivalCitiesContract.View> provideArrivalCitiesPresenter
            (ArrivalCitiesPresenter<ArrivalCitiesContract.View> presenter) {
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
    BusScheduleModel provideBusScheduleModel(AppApiClient appApiClient) {
        return new BusScheduleModel(appApiClient);
    }

    @Provides
    CitiesModel provideCitiesModel(AppApiClient appApiClient) {
        return new CitiesModel(appApiClient);
    }

    @Provides
    BookingModel provideBookingModel(AppApiClient appApiClient) {
        return new BookingModel(appApiClient);
    }

    @Provides
    UserModel provideUserModel(AppApiClient appApiClient) {
        return new UserModel(appApiClient);
    }
}
