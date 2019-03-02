package com.github.kko7.manaflux_android.UserInterface.Dashboard;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.kko7.manaflux_android.ChampionSelectService;
import com.github.kko7.manaflux_android.Helpers.PrefsHelper;
import com.github.kko7.manaflux_android.R;
import com.github.kko7.manaflux_android.UserInterface.MainActivity;

import java.io.File;

import static android.content.Context.ACTIVITY_SERVICE;

public class SettingsFragment extends Fragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();
    private PrefsHelper prefsHelper;
    private RelativeLayout layout;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        Log.d(TAG, "onView: Created");

        prefsHelper = PrefsHelper.getInstance(view.getContext());
        Button deleteDeviceButton = view.findViewById(R.id.delete_device);
        Button deleteAllButton = view.findViewById(R.id.delete_all);
        Button serviceButton = view.findViewById(R.id.service_open);
        Spinner spinner = view.findViewById(R.id.spinner);
        layout = view.findViewById(R.id.settings_layout);

        deleteDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsHelper.saveString("device-ip", null);
                prefsHelper.saveString("device-name", null);
                startActivity(new Intent(view.getContext(), MainActivity.class));
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCache(view.getContext());
                clearAppData(view.getContext());
            }
        });

        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(view.getContext());
            }
        });

        String[] items = new String[]{
                getString(R.string.background_default), //default
                getString(R.string.background_red), //red
                getString(R.string.background_purple), //purple
                getString(R.string.background_green), //green
                getString(R.string.background_gray), //purple
                getString(R.string.background_dark_red) //dark red
        };
        ArrayAdapter adapter =
                new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        spinner.setSelection(prefsHelper.getInt("spinner"));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 0:
                        prefsHelper.saveString("background", "default");
                        prefsHelper.saveInt("spinner", 0);
                        break;
                    case 1:
                        prefsHelper.saveString("background", "red");
                        prefsHelper.saveInt("spinner", 1);
                        break;
                    case 2:
                        prefsHelper.saveString("background", "purple");
                        prefsHelper.saveInt("spinner", 2);
                        break;
                    case 3:
                        prefsHelper.saveString("background", "green");
                        prefsHelper.saveInt("spinner", 3);
                        break;
                    case 4:
                        prefsHelper.saveString("background", "gray");
                        prefsHelper.saveInt("spinner", 4);
                        break;
                    case 5:
                        prefsHelper.saveString("background", "red_dark");
                        prefsHelper.saveInt("spinner", 5);
                        break;
                    default:
                        prefsHelper.saveString("background", "default");
                        prefsHelper.saveInt("spinner", 0);
                        break;
                }
                String value = prefsHelper.getBackground("background");
                int res = getResources().getIdentifier(value + "_bg", "mipmap",
                        view.getContext().getPackageName());
                layout.setBackgroundResource(res);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO
            }
        });
    }

    private static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void clearAppData(Context context) {
        try {
            ((ActivityManager)context.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_service);

        final TextView status = dialog.findViewById(R.id.status_text);
        final Button startButton = dialog.findViewById(R.id.start_service);
        final Button stopButton = dialog.findViewById(R.id.stop_service);

        if(prefsHelper.getBoolean("service-running")) {
            status.setText(getString(R.string.service_running));
        } else {
            status.setText(getString(R.string.service_not_running));
        }

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsHelper.saveBoolean("service-running", false);
                status.setText(getString(R.string.service_not_running));
                context.stopService(new Intent(context, ChampionSelectService.class));
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsHelper.saveBoolean("service-running", true);
                status.setText(getString(R.string.service_running));
                context.startService(new Intent(context, ChampionSelectService.class));
            }
        });
        dialog.show();
    }
}
