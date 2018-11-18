package com.github.kko7.manaflux_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kko7.manaflux_android.ActivityHandler.ChangeActivity;
import com.github.kko7.manaflux_android.ActivityHandler.SettingsActivity;
import com.github.kko7.manaflux_android.ListAdapter.PC;
import com.github.kko7.manaflux_android.ListAdapter.PcListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 205;
    TextView changeButton;
    TextView connectButton;
    TextView addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeButton = findViewById(R.id.edit_button);
        connectButton = findViewById(R.id.connect_button);
        addButton = findViewById(R.id.add_button);
        Log.d(TAG, "onCreate: Started.");
        initViews();
    }

    protected void initButtons() {
        Button settingsButton = findViewById(R.id.settings_button);
        Button changeButton = findViewById(R.id.edit_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChangeActivity.class));
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
        ListView mListView = findViewById(R.id.listView);

        //Create the PC objects
        PC john = new PC("John","12-20-1998");
        PC steve = new PC("Steve","08-03-1987");
        PC stacy = new PC("Stacy","11-15-2000");
        PC ashley = new PC("Ashley","07-02-1999");
        PC matt = new PC("Matt","03-29-2001");
        PC matt2 = new PC("Matt2","03-29-2001");
        PC matt3 = new PC("Matt3","03-29-2001");
        PC matt4 = new PC("Matt4","03-29-2001");
        PC matt5 = new PC("Matt5","03-29-2001");
        PC matt6 = new PC("Matt6","03-29-2001");
        PC matt7 = new PC("Matt7","03-29-2001");
        PC matt8 = new PC("Matt8","03-29-2001");
        PC matt9 = new PC("Matt9","03-29-2001");
        PC matt10 = new PC("Matt10","03-29-2001");
        PC matt11 = new PC("Matt11","03-29-2001");

        //Add the PC objects to an ArrayList
        ArrayList<PC> pcList = new ArrayList<>();
        pcList.add(john);
        pcList.add(steve);
        pcList.add(stacy);
        pcList.add(ashley);
        pcList.add(matt);
        pcList.add(matt2);
        pcList.add(matt3);
        pcList.add(matt4);
        pcList.add(matt5);
        pcList.add(matt6);
        pcList.add(matt7);
        pcList.add(matt8);
        pcList.add(matt9);
        pcList.add(matt10);
        pcList.add(matt11);

        PcListAdapter adapter = new PcListAdapter(this, R.layout.list_adapter, pcList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                connectButton.setVisibility(View.VISIBLE);
                changeButton.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
                view.setSelected(true);


            }
        });
    }

    protected void initViews() {
        changeButton.setVisibility(View.GONE);
        connectButton.setVisibility(View.GONE);
        addButton.setVisibility(View.VISIBLE);
        checkPerms();
        initList();
    }
}
