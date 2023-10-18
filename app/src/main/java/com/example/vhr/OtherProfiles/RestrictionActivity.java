package com.example.vhr.OtherProfiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vhr.Calendar.CalendarActivity;
import com.example.vhr.DoctorActivity.DisplayTreatingDoctorComponent;
import com.example.vhr.DoctorActivity.DoctorsResearchActivity;
import com.example.vhr.MainActivity;
import com.example.vhr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RestrictionActivity extends AppCompatActivity {
    private List<Profil> profils;
    ImageView calendarImageView;
    ImageView doctorsImageView;
    ImageView restrictionImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restrictions);
        FloatingActionButton addButton = findViewById(R.id.addButton);
        calendarImageView = findViewById(R.id.imageView);
        doctorsImageView = findViewById(R.id.imageView2);
        restrictionImageView = findViewById(R.id.imageView3);

        profils = ProfilSave.getProfils(this);
        if (profils == null) {
            profils = new ArrayList<>();
        }

        DisplayProfilComponent adapter = new DisplayProfilComponent(this,profils);
        ListView listViewProfil = findViewById(R.id.listViewProfil);
        listViewProfil.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestrictionActivity.this, CreationProfil.class);
                startActivity(intent);
            }
        });

        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the CalendarActivity
                Intent intent = new Intent(RestrictionActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        doctorsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the DoctorsResearchActivity
                Intent intent = new Intent(RestrictionActivity.this, DoctorsResearchActivity.class);
                startActivity(intent);
            }
        });

        restrictionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the RestrictionActivity
                Intent intent = new Intent(RestrictionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}