package org.minibus.app.di.modules;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import org.minibus.app.data.network.AppApiClient;
import org.minibus.app.data.network.model.CitiesModel;
import org.minibus.app.data.network.model.RouteScheduleModel;
import org.minibus.app.data.network.model.RoutesModel;
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
import org.minibus.app.ui.schedule.RouteScheduleContract;
import org.minibus.app.ui.schedule.RouteSchedulePresenter;
import org.minibus.app.ui.schedule.trip.RouteTripContract;
import org.minibus.app.ui.schedule.trip.RouteTripPresenter;
import org.minibus.app.ui.cities.BaseCitiesContract;
import org.minibus.app.ui.cities.BaseCitiesPresenter;

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
    RouteScheduleContract.Presenter<RouteScheduleContract.View> provideBusSchedulePresenter
            (RouteSchedulePresenter<RouteScheduleContract.View> presenter) {
        return presenter;
    }

    @Provides
    BaseCitiesContract.Presenter<BaseCitiesContract.View> provideCitiesPresenter
            (BaseCitiesPresenter<BaseCitiesContract.View> presenter) {
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
    RouteTripContract.Presenter<RouteTripContract.View> provideBusTripPresenter
            (RouteTripPresenter<RouteTripContract.View> presenter) {
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
    RouteScheduleModel provideBusScheduleModel(AppApiClient appApiClient) {
        return new RouteScheduleModel(appApiClient);
    }

    @Provides
    CitiesModel provideCitiesModel(AppApiClient appApiClient) {
        return new CitiesModel(appApiClient);
    }

    @Provides
    RoutesModel provideRoutesModel(AppApiClient appApiClient) {
        return new RoutesModel(appApiClient);
    }

    @Provides
    UserModel provideUserModel(AppApiClient appApiClient) {
        return new UserModel(appApiClient);
    }
}
