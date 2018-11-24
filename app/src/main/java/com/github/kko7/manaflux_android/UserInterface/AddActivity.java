package com.github.kko7.manaflux_android.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Database.DBHandler;
import com.github.kko7.manaflux_android.MainActivity;
import com.github.kko7.manaflux_android.R;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "Edit";
    EditText address;
    EditText name;
    ImageButton saveButton;
    ImageButton closeButton;
    Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        saveButton = findViewById(R.id.save_button);
        scanButton = findViewById(R.id.scan_button);
        closeButton = findViewById(R.id.close_button);

        address = findViewById(R.id.ip_edit);
        name = findViewById(R.id.name_edit);

        address.setText(getIntent().getStringExtra("ip_address"));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceAddress = address.getText().toString();
                String deviceName = name.getText().toString();

                DBHandler dbHandler = new DBHandler(AddActivity.this);
                dbHandler.insertDeviceDetails(deviceAddress, deviceName);
                startActivity(new Intent(AddActivity.this, MainActivity.class));
                Toast.makeText(getApplicationContext(), "Device added successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Device added IP: " + deviceAddress + " NAME: " + deviceName);
                }
            }
        );

        scanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AddActivity.this, ScanActivity.class).putExtra("type", "add"));
                }
             }
        );

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, MainActivity.class));
            }
        });
    }
}
