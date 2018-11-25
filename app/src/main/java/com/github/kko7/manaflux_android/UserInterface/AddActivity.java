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

public class AddActivity extends AppCompatActivity implements android.view.View.OnClickListener {

    ImageButton btnSave, btnClose;
    Button btnDelete;
    EditText editTextAddress, editTextName;
    private int _Device_Id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnSave = findViewById(R.id.save_button);
        btnClose = findViewById(R.id.close_button);
        btnDelete = findViewById(R.id.delete_button);

        editTextAddress = findViewById(R.id.ip_edit);
        editTextName = findViewById(R.id.name_edit);

        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnDelete.setVisibility(View.GONE);

        _Device_Id =0;
        Intent intent = getIntent();
        _Device_Id =intent.getIntExtra("device_Id", 0);

    }

    public void onClick(View view) {
        if (view == findViewById(R.id.save_button)){
            DeviceRepo repo = new DeviceRepo(this);
            Device device = new Device();
            device.address = editTextAddress.getText().toString();
            device.name = editTextName.getText().toString();
            device.device_ID = _Device_Id;
             _Device_Id = repo.insert(device);
             Toast.makeText(this,"New Device Insert", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddActivity.this, MainActivity.class));
        } else if (view== findViewById(R.id.close_button)){
            finish();
        }

    }

}
