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

import com.androidnetworking.AndroidNetworking;
import com.github.kko7.manaflux_android.Connection.ApiClient;
import com.github.kko7.manaflux_android.Connection.ApiInterface;
import com.github.kko7.manaflux_android.CustomElements.TextView;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.Models.ApiData;
import com.github.kko7.manaflux_android.Models.HeartbeatData;
import com.github.kko7.manaflux_android.Models.Spell;
import com.github.kko7.manaflux_android.R;
import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChampionSelectActivity extends AppCompatActivity {

    private static final String TAG = ChampionSelectActivity.class.getSimpleName();
    ArrayList<Spell> mData;
    private String[] positions;
    private Context context;
    private TextView code;
    private TextView error;
    private ImageButton spellButton1;
    private ImageButton spellButton2;
    private RelativeLayout errorLayout;
    private RelativeLayout layout;
    private int spell1, spell2;
    private ApiInterface client;
    private Disposable disposable;

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
    }

    private void start() {
        client = new ApiClient(this).getClient();
        final TextView championName = findViewById(R.id.champion_name);
        final ImageView championImage = findViewById(R.id.champion_image);
        final String deviceIp = PrefsHelper.getInstance(this).getString("device-ip");

        layout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        spellButton1 = findViewById(R.id.spell_button1);
        spellButton2 = findViewById(R.id.spell_button2);

        AndroidNetworking.initialize(getApplicationContext());
        Rx2AndroidNetworking.get("http://" + deviceIp + ":4500/ap1/v1/me/heartbeat")
                .addHeaders("Authorization", PrefsHelper.getInstance(this).getString("auth-token"))
                .build()
                .getObjectObservable(HeartbeatData.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(1, TimeUnit.SECONDS)
                .repeat()
                .subscribe(new Observer<HeartbeatData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        try {
                            disposable = d;
                            getSummonerSpells();
                            Thread.sleep(500);
                            getPositions();
                            getCurrentSpells();
                        } catch (Exception e) {
                            showError("Internal error", e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(final HeartbeatData heartbeatData) {
                        try {
                            if (heartbeatData.getInChampionSelect()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        championName.setText(heartbeatData.getChampionName());
                                        Picasso.get()
                                                .load(heartbeatData.getChampionImg()
                                                        .replace("localhost", deviceIp))
                                                .placeholder(R.mipmap.test)
                                                .into(championImage);
                                    }
                                });
                            } else {
                                showError("Not in champion select", "");
                                disposable.dispose();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        showException(deviceIp, e);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getApplicationContext(), "Completed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposable.dispose();
    }

    private void getSummonerSpells() {
        final Call<ApiData> summonerSpellsApi = client.getSummonerSpells();
        summonerSpellsApi.enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(@NonNull Call<ApiData> call,
                                   @NonNull final Response<ApiData> response) {
                final ApiData spellsData = response.body();
                assert spellsData != null;
                if (response.isSuccessful() && spellsData.getSuccess()) {
                    mData = spellsData.getSummonerSpells();
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
                    showError(spellsData.getError(), spellsData.getErrorCode());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable throwable) {
                showException(call.request().url().host(), throwable);
            }
        });
    }

    private void getPositions() {
        final Call<ApiData> positionsApi = client.getPositions();
        positionsApi.enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(@NonNull Call<ApiData> call,
                                   @NonNull Response<ApiData> response) {
                ApiData positionsData = response.body();
                assert positionsData != null;
                if (response.isSuccessful() && positionsData.getSuccess()) {
                    positions = positionsData.getPositions();
                    initList();
                } else {
                    showError(positionsData.getError(), positionsData.getErrorCode());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable throwable) {
                showException(call.request().url().host(), throwable);
            }
        });
    }

    private void getCurrentSpells() {
        final Call<ApiData> spellsApi = client.getCurrentSpells();
        spellsApi.enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(@NonNull Call<ApiData> call,
                                   @NonNull Response<ApiData> response) {
                assert response.body() != null;
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Spell spellById1 =
                            getSpellById(Integer.valueOf(response.body().getSpells()[0]));
                    assert spellById1 != null;
                    spell1 = spellById1.getSpellId();
                    Spell spellById2 =
                            getSpellById(Integer.valueOf(response.body().getSpells()[1]));
                    assert spellById2 != null;
                    spell2 = spellById2.getSpellId();
                    updateButtons(spellButton1, spellById1.getPath());
                    updateButtons(spellButton2, spellById2.getPath());
                } else {
                    showError(response.body().getError(), response.body().getErrorCode());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiData> call,
                                  @NonNull Throwable throwable) {
                showException(call.request().url().host(), throwable);
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
                final ApiInterface client = new ApiClient(getApplicationContext()).getClient();
                Call<ApiData> setPosition = client.setPosition(position);
                setPosition.enqueue(new Callback<ApiData>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiData> call, @NonNull Response<ApiData> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.champion_select_changed) + positions[position],
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.champion_select_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable t) {
                        showException(call.request().url().host(), t);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showDialog(final View v, ArrayList<Spell> data) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_spells);

        SpellsAdapter adapter = new SpellsAdapter(context, data);
        RecyclerView recyclerView = dialog.findViewById(R.id.spells_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

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
                                showError("HHH", "HHH");
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiData> call, @NonNull Throwable t) {
                            showException(call.request().url().host(), t);
                        }
                    });
                }
            }
        });
        recyclerView.setAdapter(adapter);
        dialog.show();
    }

    private void updateButtons(ImageButton button, String path) {
        Picasso.get()
                .load("http://" + PrefsHelper.getInstance(context).getString("device-ip") + ":" + path)
                .fit()
                .centerCrop()
                .into(button);
    }

    private Spell getSpellById(Integer id) {
        System.out.println(id);
        System.out.println(mData);
        for (Spell spell : mData) {
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

    private void showException(final String string, final Throwable throwable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String error1, error2;
                if (throwable instanceof SocketTimeoutException) {
                    error1 = "Connect timed out";
                    error2 = "Host: " + string;
                } else if (throwable instanceof UnknownHostException) {
                    error1 = "Unable to resolve host";
                    error2 = "Host: " + string;
                } else {
                    error1 = "Other exception";
                    error2 = "Contact developer";
                }
                showError(error1, error2);
            }
        });
    }
}
