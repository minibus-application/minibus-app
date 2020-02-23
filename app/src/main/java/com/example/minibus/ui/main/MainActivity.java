package com.example.minibus.ui.main;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.minibus.App;
import com.example.minibus.di.components.ActivityComponent;
import com.example.minibus.di.components.DaggerActivityComponent;
import com.example.minibus.di.modules.ActivityModule;
import com.example.minibus.ui.R;
import com.example.minibus.ui.schedule.BusScheduleFragment;
import com.example.minibus.ui.base.BackButtonListener;
import com.example.minibus.utils.NetworkChangeReceiver;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((App) getApplication()).getComponent())
                .build();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(new NetworkChangeReceiver(), filter);

        setContentView(R.layout.activity_main);

        View decor = getWindow().getDecorView();
        if (decor != null) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(0);
        }

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.main_container,
                    BusScheduleFragment.newInstance(),
                    BusScheduleFragment.class.getName());
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
