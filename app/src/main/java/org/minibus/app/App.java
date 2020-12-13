package org.minibus.app;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;
import org.minibus.app.di.components.ApplicationComponent;
import org.minibus.app.di.components.DaggerApplicationComponent;
import org.minibus.app.di.modules.ApplicationModule;
import java.util.Locale;
import timber.log.Timber;

public class App extends Application {

    protected ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        Locale.setDefault(Locale.ENGLISH);

        Timber.plant(new Timber.DebugTree());

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        applicationComponent.inject(this);
    }

    public ApplicationComponent getComponent() {
        return applicationComponent;
    }
}
