package com.example.vhr.DoctorActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vhr.R;

import java.util.ArrayList;
import java.util.List;

public class TreatingDoctorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treating_doctor);

        //We retrieve information about doctors
        List<Doctor> doctors = DoctorPreferencesSave.getDoctors(this);
        if (doctors == null) {
            doctors = new ArrayList<>();
        }

        DisplayTreatingDoctorComponent adapter = new DisplayTreatingDoctorComponent(this, doctors);

        ListView listViewDoctors = findViewById(R.id.listViewDoctors);
        listViewDoctors.setAdapter(adapter);

        Button buttonAddOrModifyDoctor = findViewById(R.id.buttonAddOrModifyDoctor);
        buttonAddOrModifyDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TreatingDoctorActivity.this, DoctorsResearchActivity.class);
                startActivity(intent);
            }
        });
    }}
