package org.minibus.app.di.components;

import android.app.Application;
import android.content.Context;

import org.minibus.app.App;
import org.minibus.app.data.local.AppStorageManager;
import org.minibus.app.di.ApplicationContext;
import org.minibus.app.di.modules.ApplicationModule;

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
