package com.github.kko7.manaflux_android.UserInterface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.github.kko7.manaflux_android.Connection.ApiInterface;
import com.github.kko7.manaflux_android.CustomElements.TextView;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.Models.ApiData;
import com.github.kko7.manaflux_android.Models.HeartbeatData;
import com.github.kko7.manaflux_android.Models.Spells;
import com.github.kko7.manaflux_android.R;
import com.github.kko7.manaflux_android.Services.NotificationsService;
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
    private ArrayList<Spells> mData;
    private int spell1, spell2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_select);
        Log.d(TAG, "onCreate: Started");

        context = this;
        code = findViewById(R.id.errorCode);
        error = findViewById(R.id.error);
        layout = findViewById(R.id.select_layout);
        errorLayout = findViewById(R.id.error_layout);
        start();
        startService(new Intent(ChampionSelectActivity.this, NotificationsService.class));
    }

    private void start() {
        final TextView championName = findViewById(R.id.champion_name);
        final ImageView championImage = findViewById(R.id.champion_image);
        final ApiInterface client = new ApiClient(this).getClient();
        final Call<HeartbeatData> heartbeatApi = client.getHeartbeat();
        final Call<ApiData> positionsApi = client.getPositions();
        final Call<ApiData> spellsApi = client.getSpells();
        final Call<ApiData> summonerSpellsApi = client.getSummonerSpells();
        layout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        spellButton1 = findViewById(R.id.spell_button1);
        spellButton2 = findViewById(R.id.spell_button2);

        heartbeatApi.enqueue(new Callback<HeartbeatData>() {
            @Override
            public void onResponse(@NonNull final Call<HeartbeatData> call,
                                   @NonNull Response<HeartbeatData> response) {
                final HeartbeatData heartbeatData = response.body();
                assert heartbeatData != null;
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            championName.setText(heartbeatData.getChampionName());
                            Picasso.get()
                                    .load(heartbeatData.getChampionImg()
                                            .replace("localhost", call.request().url().host()))
                                    .placeholder(R.mipmap.test)
                                    .into(championImage);
                        }
                    });
                    positionsApi.enqueue(new Callback<ApiData>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiData> call,
                                               @NonNull Response<ApiData> response) {
                            ApiData positionsData = response.body();
                            assert positionsData != null;
                            if (response.isSuccessful() && positionsData.getSuccess()) {
                                positions = positionsData.getPositions();
                                initList();
                                summonerSpellsApi.enqueue(new Callback<ApiData>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ApiData> call,
                                                           @NonNull final Response<ApiData> response) {
                                        final ApiData spellsData = response.body();
                                        assert spellsData != null;
                                        mData = spellsData.getSummonerSpells();
                                        if (response.isSuccessful() && spellsData.getSuccess()) {
                                            spellsApi.enqueue(new Callback<ApiData>() {
                                                @Override
                                                public void onResponse(@NonNull Call<ApiData> call,
                                                                       @NonNull Response<ApiData> response) {
                                                    assert response.body() != null;
                                                    Spells spellById1 =
                                                            getSpellById(Integer.valueOf(response.body().getSpells()[0]));
                                                    assert spellById1 != null;
                                                    spell1 = spellById1.getSpellId();
                                                    Spells spellById2 =
                                                            getSpellById(Integer.valueOf(response.body().getSpells()[1]));
                                                    assert spellById2 != null;
                                                    spell2 = spellById2.getSpellId();
                                                    updateButtons(spellButton1, spellById1.getPath());
                                                    updateButtons(spellButton2, spellById2.getPath());
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<ApiData> call,
                                                                      @NonNull Throwable throwable) {
                                                    showException(call, throwable);
                                                }
                                            });
                                            spellButton1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    showDialog(v, spellsData.getSummonerSpells());
                                                }
                                            });
                                            spellButton2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    showDialog(v, spellsData.getSummonerSpells());
                                                }
                                            });
                                        } else {
                                            showError(spellsData.getErrorCode(), spellsData.getError());
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable throwable) {
                                        showException(call, throwable);
                                    }
                                });
                            } else {
                                showError(positionsData.getErrorCode(), positionsData.getError());
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
        ArrayAdapter adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, positions);
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
                            Toast.makeText(v.getContext(),
                                    getString(R.string.champion_select_changed) + positions[position],
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(v.getContext(), getString(R.string.champion_select_error),
                                    Toast.LENGTH_SHORT).show();
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_spells);
        RecyclerView recyclerView = dialog.findViewById(R.id.spells_list);
        SpellsAdapter adapter;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new SpellsAdapter(context, data);
        adapter.setClickListener(new SpellsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position, final int id) {
                final ApiInterface client = new ApiClient(view.getContext()).getClient();
                RequestBody body;
                if (v.getId() == R.id.spell_button1) {
                    spell1 = position;
                    body = RequestBody.create(MediaType.parse("text/plain"), position + "," + spell2);
                } else {
                    spell2 = position;
                    body = RequestBody.create(MediaType.parse("text/plain"), spell1 + "," + position);
                }
                final Call<ApiData> setSpellsApi = client.setSpells(body);
                if (spell1 == spell2) {
                    Toast.makeText(getApplicationContext(), "Bad spells", Toast.LENGTH_SHORT).show();
                } else {
                    setSpellsApi.enqueue(new Callback<ApiData>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiData> call,
                                               @NonNull Response<ApiData> response) {
                            assert response.body() != null;
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Changed spell",
                                        Toast.LENGTH_SHORT).show();
                                if (v.getId() == R.id.spell_button1) {
                                    updateButtons(spellButton1, mData.get(id).getPath());
                                } else {
                                    updateButtons(spellButton2, mData.get(id).getPath());
                                }

                                dialog.cancel();
                            } else {
                                showError(response.body().getErrorCode(), response.body().getError());
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable t) {
                            showException(call, t);
                        }
                    });
                }
            }
        });
        recyclerView.setAdapter(adapter);
        dialog.show();
    }

    private void updateButtons(ImageButton button, String path) {
        Picasso.get()                                                                                //Here goes port
                .load("http://" + PrefsHelper.getInstance(context).getString("device-ip") + ":3688" + path)
                .fit()
                .centerCrop()
                .into(button);
    }

    private Spells getSpellById(Integer id) {
        for (Spells spell : mData) {
            if (spell.getSpellId() == id) {
                return spell;
            }
        }
        return null;
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
