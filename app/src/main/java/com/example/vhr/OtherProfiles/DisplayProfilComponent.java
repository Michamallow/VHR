package com.example.vhr.OtherProfiles;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vhr.R;

import java.util.ArrayList;
import java.util.List;

public class DisplayProfilComponent extends ArrayAdapter<Profil> implements View.OnLongClickListener {
    private Profil profil;
    public DisplayProfilComponent(Context context, List<Profil> doctors) {
        super(context, 0, doctors);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        profil = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_profil, parent, false);
        }
        convertView.setOnLongClickListener(this);

        ImageView profilePicture = convertView.findViewById(R.id.profilePicture);
        TextView name = convertView.findViewById(R.id.name);
        TextView birthdate = convertView.findViewById(R.id.birthdate);
        TextView bloodType = convertView.findViewById(R.id.bloodType);
        TextView allergies = convertView.findViewById(R.id.allergies);
        TextView medicalHistory = convertView.findViewById(R.id.medicalHistory);

        name.setText(profil.getName());
        birthdate.setText(profil.getBirthdate());
        bloodType.setText(profil.getBloodType());
        allergies.setText(profil.getAllergies());
        medicalHistory.setText(profil.getMedicalHistory());
        return convertView;
    }

    @Override
    public boolean onLongClick(View view) {
        return DeleteProfil(view.getContext());
    }

    private boolean DeleteProfil(Context context){
        List<Profil> profils = ProfilSave.getProfils(context);
        boolean result = profils.removeIf(tmpprofil -> tmpprofil.getName().equals(profil.getName()));
        ProfilSave.saveProfils(context,profils);
        Intent intent = new Intent(context, RestrictionActivity.class);
        context.startActivity(intent);
        return result;

    }
}
