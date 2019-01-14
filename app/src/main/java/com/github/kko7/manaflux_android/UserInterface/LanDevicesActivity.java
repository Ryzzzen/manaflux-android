package com.github.kko7.manaflux_android.UserInterface;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

public class LanDevicesActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RelativeLayout layout;
    PrefsHelper prefsHelper;
    Button otherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Log.d(TAG, "onCreate: Started.");

        prefsHelper = PrefsHelper.getInstance(this);
        otherButton = findViewById(R.id.other_button);
        layout = findViewById(R.id.select_layout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanDevicesActivity.this, OtherDevicesActivity.class));
            }
        });

        setBackground();
    }

    private void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }

}
