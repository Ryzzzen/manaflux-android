package com.github.kko7.manaflux_android.UserInterface;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.kko7.manaflux_android.CustomElements.CustomLayout;
import com.github.kko7.manaflux_android.Database.Adapter;
import com.github.kko7.manaflux_android.Database.DBAdapter;
import com.github.kko7.manaflux_android.Database.Device;
import com.github.kko7.manaflux_android.Helpers.DatabaseHelper;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SavedDevicesActivity extends AppCompatActivity {

    private static final String TAG = SavedDevicesActivity.class.getSimpleName();
    ArrayList<Device> devices = new ArrayList<>();
    Button addButton, scanButton;
    Button saveBtn; //dialog
    EditText nameEditTxt, addressEditTxt;
    RecyclerView mRecyclerView;
    DatabaseHelper dbHelper;
    CustomLayout layout;
    PrefsHelper prefsHelper;
    Adapter adapter;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_devices);
        Log.d(TAG, "onCreate: Started.");

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dbHelper = new DatabaseHelper(this);
        prefsHelper = PrefsHelper.getInstance(this);
        mRecyclerView = findViewById(R.id.otherList);
        layout = findViewById(R.id.other_layout);
        addButton = findViewById(R.id.add_button);
        scanButton = findViewById(R.id.scan_button);
        adapter = new Adapter(this, devices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });
        setBackground();
        retrieve();
    }

    private void showDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_device);

        addressEditTxt = dialog.findViewById(R.id.addressEditTxt);
        nameEditTxt = dialog.findViewById(R.id.nameEditTxt);
        saveBtn = dialog.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditTxt.getText().toString().equals("") || addressEditTxt.getText().toString().equals("")) {
                    Toast.makeText(SavedDevicesActivity.this, getString(R.string.dialog_null), Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.save(nameEditTxt.getText().toString(), addressEditTxt.getText().toString());
                    dialog.hide();
                }

            }
        });
        dialog.show();
    }

    private void startScan() {
        if (ContextCompat.checkSelfPermission(SavedDevicesActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SavedDevicesActivity.this,
                    Manifest.permission.CAMERA)) {
                Toast.makeText(SavedDevicesActivity.this, getString(R.string.save_permission), Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(SavedDevicesActivity.this,
                        new String[]{Manifest.permission.CAMERA}, 201);
            }
        } else {
            startActivity(new Intent(SavedDevicesActivity.this, ScanActivity.class));
        }
    }

    private void retrieve() {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        devices.clear();
        Cursor c = db.getAllDevices();

        while (c.moveToNext()) {
            int id = c.getInt(0);
            String address = c.getString(1);
            String name = c.getString(2);

            Device p = new Device(address, name, id);

            devices.add(p);
        }

        if (!(devices.size() < 1)) {
            mRecyclerView.setAdapter(adapter);
        }

        if (devices.size() == 0) {
            adapter.notifyDataSetChanged();
        }

        db.close();
    }

    private void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        Picasso.get()
                .load(id)
                .into(layout);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 201: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(SavedDevicesActivity.this, ScanActivity.class));
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieve();
        setBackground();
    }
}