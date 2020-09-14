package org.minibus.app.di.components;

import org.minibus.app.di.modules.ActivityModule;
import org.minibus.app.di.scopes.PerActivity;
import org.minibus.app.ui.cities.BaseCitiesFragment;
import org.minibus.app.ui.cities.arrival.ArrivalCitiesFragment;
import org.minibus.app.ui.cities.departure.DepartureCitiesFragment;
import org.minibus.app.ui.login.LoginFragment;
import org.minibus.app.ui.main.MainActivity;
import org.minibus.app.ui.profile.UserProfileFragment;
import org.minibus.app.ui.schedule.BusScheduleFragment;
import org.minibus.app.ui.schedule.trip.BusTripFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(BusScheduleFragment busScheduleFragment);

    // void inject(CitiesFragment citiesFragment);

    void inject(BaseCitiesFragment baseCitiesFragment);

    void inject(DepartureCitiesFragment departureCitiesFragment);

    void inject(ArrivalCitiesFragment arrivalCitiesFragment);

    void inject(BusTripFragment busTripFragment);

    void inject(UserProfileFragment userProfileFragment);

    void inject(LoginFragment loginFragment);
}
