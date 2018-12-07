package com.github.kko7.manaflux_android;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import com.github.kko7.manaflux_android.UserInterface.SettingsActivity;
import com.github.kko7.manaflux_android.UserInterface.SharedPrefs;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SharedPrefs sharedPrefs;
    Adapter adapter;
    ArrayList<Device> devices = new ArrayList<>();
    Button settingsButton, addButton, saveBtn, retrieveBtn;
    EditText nameEditTxt, addressEditTxt;
    RecyclerView mRecyclerView;
    RelativeLayout layout;
    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.main_layout);
        sharedPrefs = SharedPrefs.getInstance(this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d(TAG, "onCreate: Started.");
        initViews();
        setBackground();
    }

    protected void initButtons() {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    protected void checkPerms() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initButtons();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            initButtons();
        }
    }

    protected void initList() {
        adapter = new Adapter(this, devices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        retrieve();
    }

    protected void initViews() {
        addButton = findViewById(R.id.add_button);
        settingsButton = findViewById(R.id.settings_button);
        mRecyclerView = findViewById(R.id.recyclerView);
        checkPerms();
        initList();
    }

    private void showDialog() {
        d = new Dialog(this);

        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        d.setContentView(R.layout.dialog_layout);

        addressEditTxt = d.findViewById(R.id.addressEditTxt);
        nameEditTxt = d.findViewById(R.id.nameEditTxt);
        saveBtn = d.findViewById(R.id.saveBtn);
        retrieveBtn = d.findViewById(R.id.retrieveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(nameEditTxt.getText().toString(), addressEditTxt.getText().toString());
            }
        });

        retrieveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieve();
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

        db.close();

    }

    private void save(String address, String name) {

        DBAdapter db = new DBAdapter(this);
        db.openDB();
        long result = db.add(address, name);

        if (result > 0) {
            addressEditTxt.setText("");
            nameEditTxt.setText("");
        } else {
            Toast.makeText(getApplicationContext(), "Unable To Insert", Toast.LENGTH_SHORT).show();
        }

        db.close();
        retrieve();
    }

    protected void setBackground() {
        String value = sharedPrefs.getDataString("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        Log.d(TAG, String.valueOf(id));

        layout.setBackgroundResource(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieve();
        setBackground();
    }

}
