package org.minibus.app.di.components;

import org.minibus.app.di.modules.ActivityModule;
import org.minibus.app.di.scopes.PerActivity;
import org.minibus.app.ui.cities.BaseCitiesFragment;
import org.minibus.app.ui.cities.arrival.ArrivalCitiesFragment;
import org.minibus.app.ui.cities.departure.DepartureCitiesFragment;
import org.minibus.app.ui.login.LoginFragment;
import org.minibus.app.ui.main.MainActivity;
import org.minibus.app.ui.profile.UserProfileFragment;
import org.minibus.app.ui.schedule.RouteScheduleFragment;
import org.minibus.app.ui.schedule.trip.RouteTripFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(RouteScheduleFragment routeScheduleFragment);

    void inject(BaseCitiesFragment baseCitiesFragment);

    void inject(DepartureCitiesFragment departureCitiesFragment);

    void inject(ArrivalCitiesFragment arrivalCitiesFragment);

    void inject(RouteTripFragment routeTripFragment);

    void inject(UserProfileFragment userProfileFragment);

    void inject(LoginFragment loginFragment);
}
