package com.github.kko7.manaflux_android.UserInterface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Connection.ApiClient;
import com.github.kko7.manaflux_android.Connection.ApiInterface;
import com.github.kko7.manaflux_android.CustomElements.GifView;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.Models.ApiData;
import com.github.kko7.manaflux_android.R;
import com.github.kko7.manaflux_android.UserInterface.Dashboard.DashboardActivity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.SecureRandom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Integer count = 0;
    private RelativeLayout layout;
    private RelativeLayout errorLayout;
    private TextView errorLine1;
    private TextView errorLine2;
    private TextView title;
    private Button secondButton;
    private GifView loadingGif;
    private PrefsHelper prefsHelper;
    private Class newClass;


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
        secondButton = findViewById(R.id.second_button);
        loadingGif = findViewById(R.id.loading_gif);
        loadingGif.startGif(R.mipmap.loading);
        if (!getIntent().getBooleanExtra("class", false)) {
            newClass = DashboardActivity.class;
        } else {
            newClass = ChampionSelectActivity.class;
        }
        Button againButton = findViewById(R.id.refresh_button);
        againButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                start();
            }
        });
        setBackground();
        checkDevice();
    }

    private void checkDevice() {
        PrefsHelper prefsHelper = PrefsHelper.getInstance(this);
        String deviceIp = prefsHelper.getString("device-ip");
        String deviceName = prefsHelper.getString("device-name");
        String authToken = prefsHelper.getString("auth-token");

        if (authToken == null || authToken.equals("") || authToken.length() != 32) {
            String newToken = generateToken();
            prefsHelper.saveString("auth-token", newToken);
        }

        if (deviceIp == null || deviceIp.equals("") || deviceName == null || deviceName.equals("")) {
            startActivity(new Intent(MainActivity.this, LanDevicesActivity.class));
        } else {
            start();
        }
    }

    private void start() {
        if (count >= 3) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showError("Auth error", "Too many attempts");
                    secondButton.setVisibility(View.GONE);
                }
            });
        } else {
            try {
                loadingGif.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                final ApiInterface client = new ApiClient(this).getClient();

                Call<ApiData> summoner = client.getSummoner();
                summoner.enqueue(new Callback<ApiData>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiData> call,
                                           @NonNull final Response<ApiData> response) {
                        final ApiData summonerData = response.body();
                        try {
                            assert summonerData != null;
                            if (response.isSuccessful() && summonerData.getSuccess()) {
                                prefsHelper.saveString("summoner-name", summonerData.getSummonerName());
                                prefsHelper.saveInt("summoner-level", summonerData.getSummonerLevel());
                                prefsHelper.saveBoolean("service-running", true);
                                startActivity(new Intent(getApplicationContext(), newClass));
                            } else if (response.code() == 401 || response.code() == 403) {
                                final Call<ApiData> authentify = client.authentifyDevice(Build.BOARD);
                                authentify.enqueue(new Callback<ApiData>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ApiData> call,
                                                           @NonNull Response<ApiData> response) {
                                        assert response.body() != null;
                                        if (response.body().getAuthentified()) {
                                            Toast.makeText(MainActivity.this, getString(R.string.error_auth_success),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        start();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable t) {
                                        showException(call, t);
                                    }
                                });
                                start();
                            } else if (!summonerData.getSuccess()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showError(summonerData.getError(), summonerData.getErrorCode());
                                        secondButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showDetails(String.valueOf(response));
                                            }
                                        });
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showError("Message: " + response.message(),
                                                "Code: " + String.valueOf(response.code()));
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
                    public void onFailure(@NonNull final Call<ApiData> call,
                                          @NonNull final Throwable throwable) {
                        showException(call, throwable);
                    }
                });
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

    private void showException(final Call call, final Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        final String sStackTrace = sw.toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                title.setVisibility(View.VISIBLE);
                String error1, error2;
                if (throwable instanceof SocketTimeoutException) {
                    error1 = "Connect timed out";
                    error2 = "";
                } else if (throwable instanceof UnknownHostException) {
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

    @SuppressLint("SetTextI18n")
    private void showDetails(String text) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_details);
        TextView details = dialog.findViewById(R.id.details_long);
        details.setText(text);
        dialog.show();
    }

    private void setBackground() {
        String value = prefsHelper.getBackground("background");
        int id = getResources().getIdentifier(value + "_bg", "mipmap", getPackageName());
        errorLayout.setBackgroundResource(id);
        layout.setBackgroundResource(id);
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[16];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.on_back_title))
                .setMessage(getString(R.string.on_back_message))
                .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }

                })
                .setNegativeButton(getString(android.R.string.no), null)
                .show();
    }
}
