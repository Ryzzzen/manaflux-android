package com.github.kko7.manaflux_android.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Database.Device.Device;
import com.github.kko7.manaflux_android.Database.Device.DeviceRepo;
import com.github.kko7.manaflux_android.MainActivity;
import com.github.kko7.manaflux_android.R;

public class EditActivity extends AppCompatActivity implements android.view.View.OnClickListener{

    ImageButton btnSave, btnClose;
    Button btnDelete;
    EditText editTextAddress, editTextName;
    private int _Device_Id = 0;
    DeviceRepo repo = new DeviceRepo(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnSave = findViewById(R.id.save_button);
        btnDelete = findViewById(R.id.delete_button);
        btnClose = findViewById(R.id.close_button);

        editTextAddress = findViewById(R.id.ip_edit);
        editTextName = findViewById(R.id.name_edit);

        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        Intent intent = getIntent();
        _Device_Id = intent.getIntExtra("deviceID", 0);
        DeviceRepo repo = new DeviceRepo(this);
        Device device;
        device = repo.getDeviceById(_Device_Id);

        editTextAddress.setText(device.address);
        editTextName.setText(device.name);
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.save_button)){

            Device device = new Device();
            device.address = editTextAddress.getText().toString();
            device.name = editTextName.getText().toString();
            device.device_ID = _Device_Id;


             repo.update(device);
             Toast.makeText(this,"Device Record updated",Toast.LENGTH_SHORT).show();
             startActivity(new Intent(EditActivity.this, MainActivity.class));
        }else if (view == findViewById(R.id.delete_button)){
            repo.delete(_Device_Id);
            Toast.makeText(this, "Device Record Deleted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EditActivity.this, MainActivity.class));
        }else if (view == findViewById(R.id.close_button)){
            startActivity(new Intent(EditActivity.this, MainActivity.class));
        }
    }
}
