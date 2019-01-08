package com.github.kko7.manaflux_android.UserInterface;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.kko7.manaflux_android.Connection.HttpGet;
import com.github.kko7.manaflux_android.Connection.HttpListener;
import com.github.kko7.manaflux_android.CustomElements.GifView;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class LoadActivity extends AppCompatActivity implements HttpListener {

    private static final String TAG = "LoadActivity";
    PrefsHelper prefsHelper;
    RelativeLayout layout, error_layout;
    TextView error_code, error_message;
    Button refreshButton;
    String ip, name, token;
    GifView loading_gif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Log.d(TAG, "onCreate: Started.");
        init();
        setBackground();
        checkAll();
    }

    private void init() {
        prefsHelper = PrefsHelper.getInstance(this);
        layout = findViewById(R.id.load_layout);
        error_layout = findViewById(R.id.error_layout);
        error_code = findViewById(R.id.text_code);
        error_message = findViewById(R.id.text_message);
        loading_gif = findViewById(R.id.loading_gif);
        loading_gif.setGifImageResource(R.mipmap.loading);
        refreshButton = findViewById(R.id.refresh_button);
        ip = prefsHelper.getString("deviceIP");
        name = prefsHelper.getString("deviceNAME");
        token = prefsHelper.getString("token");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkAll();
            }
        });
    }

    private void setBackground() {
        String value = (prefsHelper.getBackground("background"));
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        layout.setBackgroundResource(id);
    }

    private void checkAll() {
        loading_gif.setVisibility(View.VISIBLE);
        error_layout.setVisibility(View.GONE);
        try {
            HttpGet httpGet = new HttpGet("http://" + ip + ":4500/summoner", this);
            httpGet.run();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onResponse(final Response response) {
        try {
            if (!response.isSuccessful()) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        error_layout.setVisibility(View.VISIBLE);
                        loading_gif.setVisibility(View.GONE);
                        error_code.setText("Code: " + response.code()); //English only
                        error_message.setText("Message: " + response.message()); //English only
                    }
                });
            } else {
                JSONObject data = new JSONObject(Objects.requireNonNull(response.body()).string());
                prefsHelper.saveString("summonerName", data.getString("summonerName"));
                prefsHelper.saveString("summonerLevel", data.getString("summonerLevel"));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(final IOException exception) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, exception);
                //TODO display on screen
            }
        });
    }
}
