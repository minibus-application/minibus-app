package org.minibus.app.ui.main;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.minibus.app.App;
import org.minibus.app.AppConstants;
import org.minibus.app.di.components.ActivityComponent;


import org.minibus.app.di.components.DaggerActivityComponent;
import org.minibus.app.di.modules.ActivityModule;
import org.minibus.app.ui.R;
import org.minibus.app.ui.schedule.RouteScheduleFragment;
import org.minibus.app.ui.base.BackButtonListener;
import org.minibus.app.utils.CommonUtil;
import org.minibus.app.utils.NetworkChangeReceiver;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        CommonUtil.adjustFontSize(this, (float) AppConstants.MAX_FONT_SCALE_FACTOR);

        setContentView(R.layout.activity_main);

        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((App) getApplication()).getComponent())
                .build();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new NetworkChangeReceiver(), filter);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.container,
                    RouteScheduleFragment.newInstance(),
                    RouteScheduleFragment.class.getName());
            transaction.commitNow();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = null;
        List<Fragment> fragments = fm.getFragments();

        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f.isVisible()) {
                    fragment = f;
                    break;
                }
            }
        }

        if (fragment instanceof BackButtonListener && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
