package com.example.vhr.OtherProfiles;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vhr.DoctorActivity.TreatingDoctorActivity;
import com.example.vhr.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreationProfil extends AppCompatActivity {

    private EditText nameEditText;
    private EditText birthdateEditText;
    private EditText bloodTypeEditText;
    private EditText allergiesEditText;
    private EditText medicalHistoryEditText;
    private ImageView profilePictureImageView;
    private Bitmap profilePicture;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_profil);

        // Initialize EditText fields and buttons
        profilePictureImageView = findViewById(R.id.imageButton);
        nameEditText = findViewById(R.id.nameEditText);
        birthdateEditText = findViewById(R.id.birthdateEditText);
        bloodTypeEditText = findViewById(R.id.bloodTypeEditText);
        allergiesEditText = findViewById(R.id.allergiesEditText);
        medicalHistoryEditText = findViewById(R.id.medicalHistoryEditText);
        profilePictureImageView = findViewById(R.id.imageButton);

        birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProfil();
            }
        });

        Button backButton = findViewById(R.id.backButtonProfilCreation);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RestrictionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AddProfil(){
        String name = nameEditText.getText().toString();
        String birthdate = birthdateEditText.getText().toString();
        String bloodType = bloodTypeEditText.getText().toString();
        String allergies = allergiesEditText.getText().toString();
        String medicalHistory = medicalHistoryEditText.getText().toString();
        Profil newProfile = new Profil(name, birthdate, bloodType, allergies, medicalHistory, profilePicture);
        List<Profil> listProfils = ProfilSave.getProfils(this);
        if(listProfils == null){
            listProfils = new ArrayList<>();
        }

        listProfils.add(newProfile);
        ProfilSave.saveProfils(this,listProfils);

        Intent intent = new Intent(CreationProfil.this, RestrictionActivity.class);
        startActivity(intent);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
                        birthdateEditText.setText(formattedDate);
                    }
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.show();
    }
}
