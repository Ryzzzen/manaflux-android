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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Database.Adapter;
import com.github.kko7.manaflux_android.Database.DBAdapter;
import com.github.kko7.manaflux_android.Database.Device;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

import java.util.ArrayList;

public class OtherDevicesActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ArrayList<Device> devices = new ArrayList<>();
    Button addButton, scanButton;
    Button saveBtn; //dialog
    EditText nameEditTxt, addressEditTxt;
    RecyclerView mRecyclerView;
    RelativeLayout layout;
    PrefsHelper prefsHelper;
    Adapter adapter;
    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        Log.d(TAG, "onCreate: Started.");
        init();
        initButtons();
        initList();
        setBackground();
    }

    private void initList() {
        adapter = new Adapter(this, devices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        retrieve();
    }

    private void init() {
        prefsHelper = PrefsHelper.getInstance(this);
        mRecyclerView = findViewById(R.id.otherList);
        layout = findViewById(R.id.other_layout);
        addButton = findViewById(R.id.add_button);
        scanButton = findViewById(R.id.scan_button);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initButtons() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(OtherDevicesActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(OtherDevicesActivity.this,
                            Manifest.permission.CAMERA)) {
                        Toast.makeText(OtherDevicesActivity.this, getString(R.string.other_permission), Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(OtherDevicesActivity.this,
                                new String[]{Manifest.permission.CAMERA}, 201);
                    }
                } else {
                    startActivity(new Intent(OtherDevicesActivity.this, ScanActivity.class));
                }
            }
        });
    }

    private void showDialog() {
        d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.dialog_layout);

        addressEditTxt = d.findViewById(R.id.addressEditTxt);
        nameEditTxt = d.findViewById(R.id.nameEditTxt);
        saveBtn = d.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditTxt.getText().toString().equals("") || addressEditTxt.getText().toString().equals("")) {
                    Toast.makeText(OtherDevicesActivity.this, getString(R.string.dialog_null), Toast.LENGTH_SHORT).show();
                } else {
                    save(nameEditTxt.getText().toString(), addressEditTxt.getText().toString());
                    d.hide();
                }

            }
        });

        d.show();
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

    private void save(String address, String name) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        long result = db.ADD(address, name);

        if (result > 0) {
            addressEditTxt.setText("");
            nameEditTxt.setText("");
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.db_fail), Toast.LENGTH_SHORT).show();
        }

        db.close();
        retrieve();
    }

    private void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 201: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(OtherDevicesActivity.this, ScanActivity.class));
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
