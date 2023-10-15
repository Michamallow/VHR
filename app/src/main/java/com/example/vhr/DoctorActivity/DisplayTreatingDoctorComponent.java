package com.example.vhr.DoctorActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.vhr.Calendar.Event;
import com.example.vhr.Calendar.EventSave;
import com.example.vhr.R;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Component used to display doctor list
public class DisplayTreatingDoctorComponent extends ArrayAdapter<Doctor> {
    private Doctor doctor;
    public DisplayTreatingDoctorComponent(Context context, List<Doctor> doctors) {
        super(context, 0, doctors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        doctor = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_treating_doctor, parent, false);
        }

        Event lastAppointment = new Event(new Date(0),"EventCompar","",doctor);
        List<Event> events = EventSave.getEvents(this.getContext());

        for (Event event: events) {
            if(event.getDoctor().getId() == doctor.getId()){
                if(event.getDateTime().compareTo(lastAppointment.getDateTime()) >0 ){
                    lastAppointment = event;
                }
            }
        }

        String location = "Location: ";
        location += doctor.getNumeroVoie().equals("null") ? "" : doctor.getNumeroVoie()+" ";
        location += doctor.getLibelleVoie().equals("null") ? "" : doctor.getLibelleVoie()+" ";
        location += doctor.getCodePostal().equals("null") ? "" : "\n Postal code: " + doctor.getCodePostal();

        if (location.equals("Location: ")){
            location += "No information";
        }
        String appointementInfo = "Last Appointment: No appointment found";
        if(lastAppointment.getDateTime().compareTo(new Date(0)) != 0){
            appointementInfo = "Last Appointment:" + lastAppointment.getDateTime().toString();
        }

        TextView doctorinfoframe = convertView.findViewById(R.id.doctorinfoframe);
        doctorinfoframe.setText(doctor.getLibelleProfession() + "\n" + doctor.getNom() + " " + doctor.getPrenom()+"\n"
        + location + "\n" + appointementInfo);

        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDoctor();
            }
        });

        return convertView;
    }

    //Method used when pressing the button to remove the doctor from the list
    private void DeleteDoctor() {
        List<Doctor> newListTreatingDoctor = DoctorPreferencesSave.getDoctors(this.getContext());
        if(newListTreatingDoctor == null){
            newListTreatingDoctor = new ArrayList<>();
        }

        for (Doctor tmpdoctor : newListTreatingDoctor)
        {
            if(doctor.getId() == tmpdoctor.getId()){
                newListTreatingDoctor.remove(tmpdoctor);
            }
        }
        DoctorPreferencesSave.saveDoctors(this.getContext(),newListTreatingDoctor);

        Intent intent = new Intent(getContext(), TreatingDoctorActivity.class);
        getContext().startActivity(intent);

    }
}