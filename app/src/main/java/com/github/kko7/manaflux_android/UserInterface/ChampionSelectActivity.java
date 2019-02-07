package com.github.kko7.manaflux_android.UserInterface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Connection.ApiClient;
import com.github.kko7.manaflux_android.Models.ApiData;
import com.github.kko7.manaflux_android.Connection.ApiInterface;
import com.github.kko7.manaflux_android.Connection.HeartbeatData;
import com.github.kko7.manaflux_android.Models.Spells;
import com.github.kko7.manaflux_android.CustomElements.TextView;
import com.github.kko7.manaflux_android.R;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChampionSelectActivity extends AppCompatActivity {

    private static final String TAG = ChampionSelectActivity.class.getSimpleName();
    private String[] positions;
    private Context context;
    private TextView code;
    private TextView error;
    private ImageButton spellButton1;
    private ImageButton spellButton2;
    private RelativeLayout errorLayout;
    private RelativeLayout layout;
    private int spell1 = 1, spell2 = 3;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_select);
        Log.d(TAG, "onCreate: Started");

        context = this;
        dialog = new Dialog(this);
        code = findViewById(R.id.errorCode);
        error = findViewById(R.id.error);
        layout = findViewById(R.id.select_layout);
        errorLayout = findViewById(R.id.error_layout);
        start();
    }

    private void start() {
        layout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);

        final TextView championName = findViewById(R.id.champion_name);
        final ImageView championImage = findViewById(R.id.champion_image);
        final ApiInterface client = new ApiClient(this).getClient();
        final Call<HeartbeatData> heartbeatApi = client.getHeartbeat();
        final Call<ApiData> positionsApi = client.getPositions();
        final Call<ApiData> spellsApi = client.getSpells();
        spellButton1 = findViewById(R.id.spell_button1);
        spellButton2 = findViewById(R.id.spell_button2);

        heartbeatApi.enqueue(new Callback<HeartbeatData>() {
            @Override
            public void onResponse(@NonNull Call<HeartbeatData> call, @NonNull Response<HeartbeatData> response) {
                final HeartbeatData responseBody = response.body();
                assert responseBody != null;
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            championName.setText(responseBody.getChampionName());
                        }
                    });
                    Picasso.get()
                            .load(responseBody.getChampionImg().replace("localhost", call.request().url().host()))
                            .placeholder(R.mipmap.test)
                            .into(championImage);
                    positionsApi.enqueue(new Callback<ApiData>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiData> call, @NonNull Response<ApiData> response) {
                            assert response.body() != null;
                            if (response.isSuccessful() && response.body().getSuccess()) {
                                positions = response.body().getPositions();
                                initList();
                                spellsApi.enqueue(new Callback<ApiData>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ApiData> call, @NonNull final Response<ApiData> response) {
                                        assert response.body() != null;
                                        if(response.isSuccessful() && response.body().getSuccess()) {
                                            spellButton1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    showDialog(v, response.body().getSummonerSpells());
                                                }
                                            });
                                            spellButton2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    showDialog(v, response.body().getSummonerSpells());
                                                }
                                            });
                                        } else {
                                            showError(response.body().getErrorCode(), response.body().getError());
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable throwable) {
                                        showException(call, throwable);
                                    }
                                });
                            } else {
                                showError(response.body().getErrorCode(), response.body().getError());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable throwable) {
                            showException(call, throwable);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<HeartbeatData> call, @NonNull Throwable throwable) {
                showException(call, throwable);
            }
        });
    }

    private void initList() {
        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, positions);
        Spinner positionsSpinner = findViewById(R.id.position_spinner);
        positionsSpinner.setAdapter(adapter);
        positionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, final View v, final int position, long id) {
                final ApiInterface client = new ApiClient(v.getContext()).getClient();
                Call<ApiData> setPosition = client.setPosition(position);
                setPosition.enqueue(new Callback<ApiData>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiData> call, @NonNull Response<ApiData> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(v.getContext(), getString(R.string.champion_select_changed) + positions[position], Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(v.getContext(), getString(R.string.champion_select_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable t) {
                        showException(call, t);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showDialog(final View v, ArrayList<Spells> data) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_spells);
        RecyclerView recyclerView = dialog.findViewById(R.id.spells_list);
        SpellsAdapter adapter;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 6));
        adapter = new SpellsAdapter(this, data);
        adapter.setClickListener(new SpellsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final ApiInterface client = new ApiClient(view.getContext()).getClient();
                Call<ApiData> setSpellsApi;
                RequestBody body;
                if(v.getId() == R.id.spell_button1) {
                    spell1 = position;
                    body = RequestBody.create(MediaType.parse("text/plain"), position + "," + spell2);
                } else {
                    spell2 = position;
                    body = RequestBody.create(MediaType.parse("text/plain"), spell1 + "," + position);
                }
                setSpellsApi = client.setSpells(body);
                if(spell1 == spell2) {
                    Toast.makeText(getApplicationContext(), "Bad spells", Toast.LENGTH_SHORT).show();
                } else setSpellsApi.enqueue(new Callback<ApiData>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiData> call, @NonNull Response<ApiData> response) {
                        assert response.body() != null;
                        if(response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Changed spell", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            showError(response.body().getErrorCode(), response.body().getError());
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable t) {
                        showException(call, t);
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showError(String message1, String message2) {
        errorLayout.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        code.setText(message1);
        error.setText(message2);
    }

    private void showException(final Call call, final Throwable throwable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                String error1, error2;
                if (throwable instanceof SocketTimeoutException) {
                    error1 = "Connect timed out";
                    error2 = "Host: " + call.request().url().host();
                } else if (throwable instanceof UnknownHostException) {
                    error1 = "Unable to resolve host";
                    error2 = "Host: " + call.request().url().host();
                } else {
                    error1 = "Other exception";
                    error2 = "Contact developer";
                }
                showError(error1, error2);
            }
        });
    }
}
