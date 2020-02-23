package com.example.minibus.di.modules;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minibus.data.network.model.BookingModel;
import com.example.minibus.data.network.model.CitiesModel;
import com.example.minibus.data.network.model.BusScheduleModel;
import com.example.minibus.data.network.model.UserModel;
import com.example.minibus.di.ActivityContext;
import com.example.minibus.ui.login.LoginContract;
import com.example.minibus.ui.login.LoginPresenter;
import com.example.minibus.ui.profile.UserProfileContract;
import com.example.minibus.ui.profile.UserProfilePresenter;
import com.example.minibus.ui.schedule.BusScheduleContract;
import com.example.minibus.ui.schedule.BusSchedulePresenter;
import com.example.minibus.ui.schedule.trip.BusTripContract;
import com.example.minibus.ui.schedule.trip.BusTripPresenter;
import com.example.minibus.ui.stops.BusStopsContract;
import com.example.minibus.ui.stops.BusStopsPresenter;

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
