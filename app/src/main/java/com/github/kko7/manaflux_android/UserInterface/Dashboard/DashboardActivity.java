package com.github.kko7.manaflux_android.UserInterface.Dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.kko7.manaflux_android.R;

public class DashboardActivity extends AppCompatActivity {

    Fragment newFragment = new ProfileFragment();
    int currentFragment = 0, selectedFragment;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        changeFragment(newFragment);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_profile:
                                newFragment = new ProfileFragment();
                                selectedFragment = 0;
                                break;
                            case R.id.nav_settings:
                                newFragment = new SettingsFragment();
                                selectedFragment = 1;
                                break;
                        }

                        if (currentFragment != selectedFragment) {
                            currentFragment = selectedFragment;
                            changeFragment(newFragment);
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}