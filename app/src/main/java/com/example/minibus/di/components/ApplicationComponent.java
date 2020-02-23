package com.example.minibus.di.components;

import android.app.Application;
import android.content.Context;

import com.example.minibus.App;
import com.example.minibus.data.local.AppStorageManager;
import com.example.minibus.di.ApplicationContext;
import com.example.minibus.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(App app);

    @ApplicationContext
    Context getApplicationContext();

    Application getApplication();

    AppStorageManager getStorageManager();
}
