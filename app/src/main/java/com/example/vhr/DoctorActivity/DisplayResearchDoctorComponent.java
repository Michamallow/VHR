package com.example.vhr.DoctorActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.vhr.R;

import java.util.ArrayList;
import java.util.List;

//Component used to display the list of wanted doctors
public class DisplayResearchDoctorComponent extends ArrayAdapter<Doctor> {
    public Doctor doctor;

    public DisplayResearchDoctorComponent(Context context, List<Doctor> doctors) {
        super(context, 0, doctors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        doctor = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_research_doctor, parent, false);
        }

        TextView doctorinfoframe = convertView.findViewById(R.id.doctorinfoframe);
        doctorinfoframe.setText(doctor.getLibelleProfession()+ " : "+doctor.getNom() + " " + doctor.getPrenom());

        Button saveButton = convertView.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDoctor();
            }
        });

        return convertView;
    }

    //Method to call when clicking on the button to save the doctor
    private void AddDoctor() {
        List<Doctor> newListTreatingDoctor = DoctorPreferencesSave.getDoctors(this.getContext());
        if(newListTreatingDoctor == null){
            newListTreatingDoctor = new ArrayList<>();
        }
        newListTreatingDoctor.add(doctor);
        DoctorPreferencesSave.saveDoctors(this.getContext(),newListTreatingDoctor);

        Intent intent = new Intent(getContext(), TreatingDoctorActivity.class);
        getContext().startActivity(intent);
    }
}
