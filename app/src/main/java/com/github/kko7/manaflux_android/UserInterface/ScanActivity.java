package com.github.kko7.manaflux_android.UserInterface;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kko7.manaflux_android.Helpers.DatabaseHelper;
import com.github.kko7.manaflux_android.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = "ScanActivity";
    private static final int CAMERA_PERMISSION = 201;
    private CameraSource cameraSource;
    private String name;
    private String ip;
    private Boolean isCorrect = false;
    private SurfaceView surfaceView;
    private TextView txtBarcodeValue;
    private Button btnAction;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Log.d(TAG, "onCreate: Started");

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dbHelper = new DatabaseHelper(this);
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.buttonAction);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCorrect) {
                    cameraSource.stop();
                    dbHelper.save(ip, name);
                    finish();
                }
            }
        });
    }

    private void initialiseDetectorsAndSources() {

        BarcodeDetector qrDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, qrDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanActivity.this, new
                                String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        qrDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), getString(R.string.scan_stop), Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qr = detections.getDetectedItems();
                if (qr.size() != 0) {

                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {
                            String data = String.valueOf((qr.valueAt(0).displayValue));

                            if (data.equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.scan_invalid),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                txtBarcodeValue.removeCallbacks(null);
                                String[] data1 = data.split(":", 2);
                                ip = data1[0];
                                name = data1[1];
                                isCorrect = true;
                                btnAction.setText(getString(R.string.scan_save));
                                txtBarcodeValue.setText(data);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
