package com.github.kko7.manaflux_android.ActivityHandler;

import android.app.Activity;
import android.os.Bundle;

import com.github.kko7.manaflux_android.ConnectionHandler.GetSummonerInfo;
import com.github.kko7.manaflux_android.R;

public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        String ipAddress = getIntent().getStringExtra("ip_address");
        GetSummonerInfo task = new GetSummonerInfo();
        task.execute("http://" + ipAddress + ":4500/summoner");

    }
}