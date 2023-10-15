package com.example.vhr.OtherProfiles;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.vhr.Calendar.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfilSave {
    private static final String PREF_NAME = "ProfilSave";
    private static final String KEY_PROFIL = "profil";

    // Method to save the Profil with SharedPreferences
    public static void saveProfils(Context context, List<Profil> profils) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(profils);
        editor.putString(KEY_PROFIL, json);
        editor.apply();
    }

    // Method to retrieve the Profil with SharedPreferences
    public static List<Profil> getProfils(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_PROFIL, null);
        Type type = new TypeToken<ArrayList<Profil>>() {}.getType();
        return gson.fromJson(json, type);
    }

}
