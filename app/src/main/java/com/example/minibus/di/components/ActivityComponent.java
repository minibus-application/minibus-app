package com.example.minibus.di.components;

import com.example.minibus.di.modules.ActivityModule;
import com.example.minibus.di.scopes.PerActivity;
import com.example.minibus.ui.login.LoginFragment;
import com.example.minibus.ui.main.MainActivity;
import com.example.minibus.ui.profile.UserProfileFragment;
import com.example.minibus.ui.schedule.BusScheduleFragment;
import com.example.minibus.ui.schedule.trip.BusTripFragment;
import com.example.minibus.ui.stops.BusStopsFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(BusScheduleFragment busScheduleFragment);

    void inject(BusStopsFragment busStopsFragment);

    void inject(BusTripFragment busTripFragment);

    void inject(UserProfileFragment userProfileFragment);

    void inject(LoginFragment loginFragment);
}
