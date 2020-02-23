package com.example.minibus.di.modules;

import android.app.Application;
import android.content.Context;

import com.example.minibus.data.local.AppStorageManager;
import com.example.minibus.di.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Singleton
    @Provides
    public AppStorageManager provideStorageManager(@ApplicationContext Context context) {
        return new AppStorageManager(context);
    }
}
