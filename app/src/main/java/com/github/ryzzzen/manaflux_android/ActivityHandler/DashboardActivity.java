package com.github.ryzzzen.manaflux_android.ActivityHandler;

import android.app.Activity;
import android.os.Bundle;

import com.github.ryzzzen.manaflux_android.ConnectionHandler.HttpGetRequest;
import com.github.ryzzzen.manaflux_android.R;

public class DashboardActivity extends Activity {
    TextView statusLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        String ipAddress = getIntent().getStringExtra("ip_address");
        statusLabel = findViewById(R.id.buttonAction);
        HttpGetRequest task = new HttpGetRequest(statusLabel);
        task.execute("http://" + ipAddress + ":4500/summoner");

    }
}
