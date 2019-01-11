package com.github.kko7.manaflux_android.UserInterface;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class DashboardActivity extends Activity {

    /* Place holder for navbar and fragments*/
    RelativeLayout layout;
    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();
        setBackground(); //FIXME Background
    }

    private void init() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        layout = findViewById(R.id.dashboard_layout);
        prefsHelper = PrefsHelper.getInstance(this);
    }

    private void setBackground() {
        String value = prefsHelper.getString("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }

}