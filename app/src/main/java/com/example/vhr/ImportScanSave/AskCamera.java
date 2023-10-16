package com.example.vhr.ImportScanSave;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vhr.R;

public class AskCamera extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_scan_import_layout);

        RadioGroup radioGroup = findViewById(R.id.radioOptions);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }

        // Set a listener for the radio buttons
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.takePhotoRadioButton) {
                openCameraActivity();
            }
        });
    }

    private void openCameraActivity() {
        // Check if the camera permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(AskCamera.this, "Camera permission not granted.", Toast.LENGTH_LONG).show();
        }
    }
}