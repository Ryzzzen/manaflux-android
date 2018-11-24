package com.github.kko7.manaflux_android.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.github.kko7.manaflux_android.Database.DBHandler;
import com.github.kko7.manaflux_android.MainActivity;
import com.github.kko7.manaflux_android.R;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "Edit";
    EditText address;
    EditText name;
    ImageButton saveButton;
    ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        saveButton = findViewById(R.id.save_button);
        address = findViewById(R.id.ip_edit);
        closeButton = findViewById(R.id.close_button);
        name = findViewById(R.id.name_edit);
        address.setText(getIntent().getStringExtra("ip_address"));
        final String deviceID = getIntent().getStringExtra("deviceID");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String deviceAddress = address.getText().toString();
            String deviceName = name.getText().toString();
            DBHandler dbHandler = new DBHandler(EditActivity.this);
            dbHandler.UpdateDeviceDetails(Integer.parseInt(deviceID), deviceAddress, deviceName);
            startActivity(new Intent(EditActivity.this, MainActivity.class));
            Log.d(TAG, "Device added IP: " + deviceAddress + " NAME: " + deviceName);
                }

            }
        );

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditActivity.this, MainActivity.class));
            }
        });
    }

}
