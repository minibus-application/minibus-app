package com.example.minibus;

import android.app.Application;

import com.example.minibus.di.components.ApplicationComponent;
import com.example.minibus.di.components.DaggerApplicationComponent;
import com.example.minibus.di.modules.ApplicationModule;

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
