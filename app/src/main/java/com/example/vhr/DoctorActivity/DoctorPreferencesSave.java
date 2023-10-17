package com.example.vhr.DoctorActivity;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//Class used to manage the saving and recovery of information relating to the user's doctors
public class DoctorPreferencesSave {
    private static final String PREF_NAME = "DoctorPrefs";
    private static final String KEY_DOCTORS = "doctors";

    // Method to save list of doctors in SharedPreferences
    public static void saveDoctors(Context context, List<Doctor> doctors) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(doctors);
        editor.putString(KEY_DOCTORS, json);
        editor.apply();
    }

    // Method to retrieve the list of doctors from SharedPreferences
    public static List<Doctor> getDoctors(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_DOCTORS, null);
        Type type = new TypeToken<ArrayList<Doctor>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
