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

import com.github.kko7.manaflux_android.Database.DBAdapter;
import com.github.kko7.manaflux_android.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = "ScanActivity";

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    public BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    boolean isIP = false;
    Button btnAction;
    String name, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        init();
        initButtons();
    }

    private void init() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.buttonAction);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initButtons() {
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ip.length() > 0 && name.length() > 0) {
                    if (isIP) {
                        save(ip, name);
                    }
                }
            }
        });
    }

    private void initialiseDetectorsAndSources() {
        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            String data = String.valueOf((barcodes.valueAt(0).displayValue));
                            Log.d(TAG, data);

                            if (data.equals("")) {
                                Toast.makeText(getApplicationContext(), "IP Address is not valid", Toast.LENGTH_LONG).show();
                            } else {
                                txtBarcodeValue.removeCallbacks(null);
                                String[] data1 = data.split(":", 2);
                                ip = data1[0];
                                name = data1[1];
                                isIP = true;
                                btnAction.setText(getString(R.string.qr_save));
                                txtBarcodeValue.setText(data);
                            }
                        }
                    });
                }
            }
        });
    }

    private void save(String address, String name) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        long result = db.ADD(address, name);

        if (result > 0) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Unable To Insert", Toast.LENGTH_SHORT).show();
        }

        db.close();
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
