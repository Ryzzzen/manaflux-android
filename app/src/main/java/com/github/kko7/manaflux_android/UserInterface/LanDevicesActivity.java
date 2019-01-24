package com.github.kko7.manaflux_android.UserInterface;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.kko7.manaflux_android.CustomElements.CustomLayout;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;
import com.squareup.picasso.Picasso;

public class LanDevicesActivity extends AppCompatActivity {

    private static final String TAG = LanDevicesActivity.class.getSimpleName();
    CustomLayout layout;
    PrefsHelper prefsHelper;
    Button savedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_devices);
        Log.d(TAG, "onCreate: Started.");

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefsHelper = PrefsHelper.getInstance(this);
        savedButton = findViewById(R.id.saved_button);
        layout = findViewById(R.id.lan_devices_layout);

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanDevicesActivity.this, SavedDevicesActivity.class));
            }
        });
        setBackground();
    }

    private void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        Picasso.get()
                .load(id)
                .centerCrop()
                .into(layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }
}
