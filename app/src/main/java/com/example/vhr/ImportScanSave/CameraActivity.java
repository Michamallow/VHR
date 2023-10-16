package com.example.vhr.ImportScanSave;
import com.example.vhr.R;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    private androidx.camera.view.PreviewView cameraPreviewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageButton captureButton;
    private ImageCapture imageCapture; // Store the ImageCapture use case

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private String capturedPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraPreviewView = findViewById(R.id.cameraPreview);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        captureButton = findViewById(R.id.captureButton);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePhoto();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            initCamera();
        }

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraUseCases(cameraProvider);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void initCamera() {
        if (cameraProviderFuture != null) {
            cameraProviderFuture.addListener(() -> {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    CameraSelector cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();

                    // Create the ImageCapture use case
                    imageCapture = new ImageCapture.Builder()
                            .setTargetRotation(Surface.ROTATION_0)
                            .build();

                    // Now that the camera is bound, you can proceed with binding the preview and imageCapture
                    bindCameraUseCases(cameraProvider);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, ContextCompat.getMainExecutor(this));
        }
    }

    private void bindCameraUseCases(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(cameraPreviewView.getSurfaceProvider());

        // Bind both preview and imageCapture to the camera
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCamera();
            } else {
                Toast.makeText(CameraActivity.this, "Camera permission not granted.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void capturePhoto() {
        ImageCapture imageCapture = new ImageCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE);
        String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";

        File folder = new File(getFilesDir(), "ImportedDocuments");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File destinationFile = new File(folder, fileName);
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(destinationFile).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                capturedPhotoPath = destinationFile.getAbsolutePath();
                returnToMainActivityWithPhoto();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(CameraActivity.this, "Failed to capture photo: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                exception.printStackTrace();
            }
        });
    }

    private void returnToMainActivityWithPhoto() {
        Intent intent = new Intent();
        intent.putExtra("captured_photo_path", capturedPhotoPath);
        setResult(RESULT_OK, intent);
        finish();
    }
}