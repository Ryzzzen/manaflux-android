package com.github.kko7.manaflux_android.UserInterface;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class DashboardActivity extends Activity {

    RelativeLayout layout;
    PrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        layout = findViewById(R.id.dashboard_layout);
        prefsHelper = PrefsHelper.getInstance(this);
        setBackground();
    }

    protected void setBackground() {
        String value = prefsHelper.getString("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }
}