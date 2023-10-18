package com.example.vhr;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.vhr.Calendar.CalendarActivity;
import com.example.vhr.DoctorActivity.DoctorsResearchActivity;
import com.example.vhr.ImportScanSave.CameraActivity;
import com.example.vhr.ImportScanSave.PopupDialog;
import com.example.vhr.OtherProfiles.RestrictionActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMPORT_FILE = 123;
    private final ArrayList<File> importedFiles = new ArrayList<>(); // List to track imported files
    private String importFileName;

    // Buttons for user's personal information
    private Button bloodTypeButton;
    private Button birthDateButton;
    private Button organDonorButton;
    private Button medicalRestrictButton;
    private Button weightButton;
    private Button heightButton;

    ImageView calendarImageView;
    ImageView doctorsImageView;
    ImageView restrictionImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the buttons by their IDs
        bloodTypeButton = findViewById(R.id.button1);
        birthDateButton = findViewById(R.id.button2);
        organDonorButton = findViewById(R.id.button3);
        medicalRestrictButton = findViewById(R.id.button4);
        weightButton = findViewById(R.id.button5);
        heightButton = findViewById(R.id.button6);

        // Same for image views
        calendarImageView = findViewById(R.id.imageView);
        doctorsImageView = findViewById(R.id.imageView2);
        restrictionImageView = findViewById(R.id.imageView3);

        // Set click listeners for the buttons and imageView elements
        bloodTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(bloodTypeButton, "Blood Type", bloodTypeButton.getText().toString());
            }
        });

        birthDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(birthDateButton, "Date of Birth", birthDateButton.getText().toString());
            }
        });

        organDonorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrganeDonatorDialog(organDonorButton);
            }
        });

        medicalRestrictButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(medicalRestrictButton, "Medical Restrictions", medicalRestrictButton.getText().toString());
            }
        });

        weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(weightButton, "Weight", weightButton.getText().toString());
            }
        });

        heightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(heightButton, "Height", heightButton.getText().toString());
            }
        });

        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the CalendarActivity
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        doctorsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the DoctorsResearchActivity
                Intent intent = new Intent(MainActivity.this, DoctorsResearchActivity.class);
                startActivity(intent);
            }
        });

        restrictionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the RestrictionActivity
                Intent intent = new Intent(MainActivity.this, RestrictionActivity.class);
                startActivity(intent);
            }
        });

        Button scanImportButton = findViewById(R.id.scanImportButton);
        scanImportButton.setOnClickListener(view -> showPopupDialog());

        loadImportedFiles();
        loadPersonalInformation();
    }

    private void showPopupDialog() {
        PopupDialog dialog = new PopupDialog(this, new PopupDialog.OnOptionSelectedListener() {
            @Override
            public void onOptionSelected(String fileName, PopupDialog.Option option) {
                // Handle the selected option and file name here
                if (option == PopupDialog.Option.SCAN) {
                    // Start the CameraActivity to capture a photo
                    Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                    intent.putExtra("file_name", fileName); // Pass the file name to CameraActivity
                    startActivity(intent);
                } else if (option == PopupDialog.Option.IMPORT) {
                    importFileName = fileName; // Store the file name
                    openFilePicker();
                }
            }
        });
        dialog.show();
    }

    private void showInputDialog(final Button button, String title, String currentText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        final View view = getLayoutInflater().inflate(R.layout.dialog_input, null);
        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText input = view.findViewById(R.id.editText);
                String newText = input.getText().toString();
                button.setText(newText);

                // Save user's personal information when they input something
                savePersonalInformation(button, newText);
            }
        });

        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showOrganeDonatorDialog(final Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Organ Donor");

        // Inflate the custom dialog layout
        View view = getLayoutInflater().inflate(R.layout.dialog_organ_donator, null);
        builder.setView(view);

        // Find the RadioGroup and RadioButtons in the layout
        RadioGroup organDonatorRadioGroup = view.findViewById(R.id.organDonatorRadioGroup);
        RadioButton yesRadioButton = view.findViewById(R.id.yesRadioButton);
        RadioButton noRadioButton = view.findViewById(R.id.noRadioButton);

        // Set a click listener for the RadioButtons
        yesRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Yes" selection
                button.setText("Yes");
                savePersonalInformation(button, "Yes");
            }
        });

        noRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "No" selection
                button.setText("No");
                savePersonalInformation(button, "No");
            }
        });

        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void savePersonalInformation(Button button, String newValue) {
        SharedPreferences preferences = getSharedPreferences("PersonalInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Use the button's ID as the key for SharedPreferences
        editor.putString(String.valueOf(button.getId()), newValue);
        editor.apply();
    }

    private void loadPersonalInformation() {
        SharedPreferences preferences = getSharedPreferences("PersonalInfo", MODE_PRIVATE);

        // Load and set user's personal information for each button
        loadPersonalInfoForButton(bloodTypeButton);
        loadPersonalInfoForButton(birthDateButton);
        loadPersonalInfoForButton(organDonorButton);
        loadPersonalInfoForButton(medicalRestrictButton);
        loadPersonalInfoForButton(weightButton);
        loadPersonalInfoForButton(heightButton);
    }

    private void loadPersonalInfoForButton(Button button) {
        SharedPreferences preferences = getSharedPreferences("PersonalInfo", MODE_PRIVATE);
        String savedValue = preferences.getString(String.valueOf(button.getId()), "Not Set");
        button.setText(savedValue);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types for import
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, REQUEST_CODE_IMPORT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMPORT_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedFileUri = data.getData();

                if (selectedFileUri != null) {
                    try {
                        // Use the provided file name or generate a unique name
                        String fileName = importFileName;
                        if (fileName == null || fileName.isEmpty()) {
                            fileName = generateUniqueFileName();
                        }

                        // Create the "ImportedDocuments" folder if it doesn't exist
                        File folder = new File(getFilesDir(), "ImportedDocuments");
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }

                        // Create a new File object with the destination path
                        File destinationFile = new File(folder, fileName);

                        // Copy the imported file to the destination
                        InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                        OutputStream outputStream = Files.newOutputStream(destinationFile.toPath());
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        inputStream.close();
                        outputStream.close();

                        // Clear the stored import file name
                        importFileName = null;

                        importedFiles.add(destinationFile);
                        updateImportedFilesView();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // Load and display imported files when the app starts
    private void loadImportedFiles() {
        File folder = new File(getFilesDir(), "ImportedDocuments");
        File[] files = folder.listFiles();

        if (files != null) {
            importedFiles.addAll(Arrays.asList(files));
        }

        updateImportedFilesView();
    }

    // Dynamically generate views for imported files and add them to the DocumentList layout
    private void updateImportedFilesView() {
        LinearLayout documentList = findViewById(R.id.DocumentList);
        documentList.removeAllViews(); // Clear the existing views

        for (File file : importedFiles) {
            if (file.isFile()) {
                LinearLayout fileLayout = new LinearLayout(this);
                fileLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                Button fileButton = new Button(this);
                fileButton.setText(file.getName());
                fileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDocument(file);
                    }
                });

                Button removeButton = new Button(this);
                removeButton.setText("X");
                fileButton.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0.75f
                ));
                fileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDocument(file);
                    }
                });

                Button xButton = new Button(this);
                xButton.setText("X");
                xButton.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0.25f
                ));
                xButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeFile(file);
                    }
                });

                fileLayout.addView(fileButton);
                fileLayout.addView(xButton);
                documentList.addView(fileLayout);
            }
        }
    }

    // Method to remove a file from app storage
    private void removeFile(File file) {
        if (file.exists()) {
            if (file.delete()) {
                importedFiles.remove(file);
                updateImportedFilesView();
            } else {
                Toast.makeText(MainActivity.this, "File couldn't be removed", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Open the document using an appropriate application
    private void openDocument(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
        String mimeType = getMimeType(file);

        intent.setDataAndType(uri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "No corresponding applications were found.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // Get the MIME type of the file
    private String getMimeType(File file) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    // Generate a unique file name if the user didn't specify one
    private String generateUniqueFileName() {
        return "imported_file_" + System.currentTimeMillis();
    }
}