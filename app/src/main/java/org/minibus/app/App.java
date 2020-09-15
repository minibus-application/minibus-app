package org.minibus.app;

import android.app.Application;

import org.minibus.app.di.components.ApplicationComponent;


import org.minibus.app.di.components.DaggerApplicationComponent;
import org.minibus.app.di.modules.ApplicationModule;

import timber.log.Timber;

public class App extends Application {

    protected ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent(){
        return applicationComponent;
    }
}
