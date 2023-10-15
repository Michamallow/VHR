package com.example.vhr.OtherProfiles;

import android.graphics.Bitmap;

public class Profil {
    String name;
    String birthdate;
    String bloodType;
    String allergies;
    String medicalHistory;
    Bitmap profilePicture;

    public Profil(String name, String birthdate, String bloodType, String allergies, String medicalHistory, Bitmap profilePicture) {
        this.name = name;
        this.birthdate = birthdate;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.medicalHistory = medicalHistory;
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }
}
