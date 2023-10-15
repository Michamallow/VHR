package com.example.vhr.OtherProfiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vhr.DoctorActivity.DisplayTreatingDoctorComponent;
import com.example.vhr.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RestrictionActivity extends AppCompatActivity {
private List<Profil> profils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restrictions);
        FloatingActionButton addButton = findViewById(R.id.addButton);

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
    }

}