package com.github.kko7.manaflux_android.UserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.kko7.manaflux_android.ConnectionHandler.GetSummonerInfo;
import com.github.kko7.manaflux_android.Database.Device.Device;
import com.github.kko7.manaflux_android.Database.Device.DeviceHandler;
import com.github.kko7.manaflux_android.Database.Device.DeviceRepo;
import com.github.kko7.manaflux_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class DashboardActivity extends Activity {
    private int _Device_Id = 0;
    private String deviceAddress;
    private String deviceName;
    TextView nameLabel;
    TextView levelLabel;
    String summonerName;
    String summonerLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        nameLabel = findViewById(R.id.dashboard_summonerName);
        levelLabel = findViewById(R.id.dashboard_summonerLevel);
        _Device_Id = intent.getIntExtra("deviceID", 0);
        DeviceRepo repo = new DeviceRepo(this);
        Device device;
        device = repo.getDeviceById(_Device_Id);
        deviceAddress = device.address;
        deviceName = device.name;
        String url = "http://" + deviceAddress + ":4500/summoner";
        GetSummonerInfo task = new GetSummonerInfo();
        task.execute(url);
        try {
            String result = String.valueOf(task.get());
            JSONObject summonerInfo = new JSONObject(result);
            summonerName = summonerInfo.getString("summonerName");
            summonerLevel = summonerInfo.getString("summonerLevel");

        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        nameLabel.setText(summonerName);
        levelLabel.setText(summonerLevel);
    }
}