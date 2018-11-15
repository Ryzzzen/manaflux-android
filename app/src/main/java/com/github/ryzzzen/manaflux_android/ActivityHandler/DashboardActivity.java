package com.github.ryzzzen.manaflux_android.ActivityHandler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.ryzzzen.manaflux_android.ConnectionHandler.HttpGetRequest;
import com.github.ryzzzen.manaflux_android.R;

public class DashboardActivity extends Activity {
	public TextView nameView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        String ipAddress = getIntent().getStringExtra("ip_address");
		
		nameView = findViewById(R.id.summonerNaem_view);
        
        HttpGetRequest task = new HttpGetRequest();
        task.execute("http://" + ipAddress + ":4500/summoner");

    }
	
	public setName(String name) {
		nameView.setText(name);
	}
}
