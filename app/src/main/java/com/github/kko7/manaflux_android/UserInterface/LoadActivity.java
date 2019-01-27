package com.github.kko7.manaflux_android.UserInterface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.kko7.manaflux_android.Connection.HttpGet;
import com.github.kko7.manaflux_android.Connection.HttpListener;
import com.github.kko7.manaflux_android.Connection.HttpPost;
import com.github.kko7.manaflux_android.CustomElements.GifView;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;
import com.github.kko7.manaflux_android.UserInterface.Dashboard.DashboardActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

public class LoadActivity extends AppCompatActivity {

    private static final String TAG = LoadActivity.class.getSimpleName();
    int count = 0;
    String ip, name, token;
    RelativeLayout layout, errorLayout;
    TextView errorLine1, errorLine2, details, title;
    Button againButton, secondButton;
    GifView loadingGif;
    PrefsHelper prefsHelper;
    Context context;
    Class newClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Log.d(TAG, "onCreate: Started");

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        prefsHelper = PrefsHelper.getInstance(this);
        layout = findViewById(R.id.load_layout);
        errorLayout = findViewById(R.id.error_layout);
        errorLine1 = findViewById(R.id.text_error1);
        errorLine2 = findViewById(R.id.text_error2);
        title = findViewById(R.id.error_title);
        againButton = findViewById(R.id.refresh_button);
        secondButton = findViewById(R.id.second_button);
        loadingGif = findViewById(R.id.loading_gif);
        loadingGif.startGif(R.mipmap.loading);
        ip = prefsHelper.getString("device-ip");
        name = prefsHelper.getString("device-name");
        token = prefsHelper.getString("phone-token");
        if (getIntent().getStringExtra("class") == null) newClass = DashboardActivity.class;
        else newClass = ChampionSelectActivity.class;
        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                start();
            }
        });
        context = this;
        setBackground();
        start();
    }

    private void start() {
        Log.d(TAG, String.valueOf(count));
        if (count >= 3) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showError("Auth error", "Too many attempts");
                    secondButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDetails("TODO");
                        }
                    });
                }
            });
        } else {
            try {
                loadingGif.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                HttpGet httpGet = new HttpGet("http://" + ip + ":4500/api/v1/summoner", new HttpListener() {
                    @Override
                    public void onResponse(final Call call, final Response response) {
                        try {
                            if (response.isSuccessful()) {
                                JSONObject data = new JSONObject(Objects.requireNonNull(response.body()).string());
                                prefsHelper.saveString("summonerName", data.getString("summonerName"));
                                prefsHelper.saveString("summonerLevel", data.getString("summonerLevel"));
                                startActivity(new Intent(LoadActivity.this, newClass));
                            } else if (response.code() == 401 || response.code() == 403) {
                                HttpPost sendAuth = new HttpPost("http://" + ip + ":4500/api/v1/authentify/Android/" + Build.DEVICE, Build.MODEL, context);
                                sendAuth.run(5, 5);
                                start();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showError("Message: " + response.message(), "Code: " + String.valueOf(response.code()));
                                        secondButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showDetails(String.valueOf(response));
                                            }
                                        });
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(final Call call, final IOException exception) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        exception.printStackTrace(pw);
                        final String sStackTrace = sw.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                title.setVisibility(View.VISIBLE);
                                String error1, error2;
                                if (exception instanceof SocketTimeoutException) {
                                    error1 = "Connect timed out";
                                    error2 = "";
                                } else if (exception instanceof UnknownHostException) {
                                    error1 = "Unable to resolve host";
                                    error2 = "Host: " + call.request().url().host();
                                } else {
                                    error1 = "Other exception";
                                    error2 = "Contact developer";
                                }
                                showError(error1, error2);

                                secondButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showDetails(sStackTrace);
                                    }
                                });

                            }
                        });
                    }
                }, context);
                httpGet.run(5, 5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        count += 1;
    }

    @SuppressLint("SetTextI18n")
    private void showError(String message1, String message2) {
        secondButton.setVisibility(View.VISIBLE);
        secondButton.setText(getString(R.string.error_details));
        errorLayout.setVisibility(View.VISIBLE);
        loadingGif.setVisibility(View.GONE);
        errorLine1.setText(message1);
        errorLine2.setText(message2);
    }

    @SuppressLint("SetTextI18n")
    private void showDetails(String text) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_details);
        details = dialog.findViewById(R.id.details_long);
        details.setText(text);
        dialog.show();
    }

    private void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        errorLayout.setBackgroundResource(id);
        layout.setBackgroundResource(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
        count = 0;
        start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}