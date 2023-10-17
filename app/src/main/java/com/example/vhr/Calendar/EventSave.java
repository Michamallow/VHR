package com.example.vhr.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vhr.DoctorActivity.Doctor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventSave {
    private static final String PREF_NAME = "EventSave";
    private static final String KEY_EVENT = "Event";

    // Method to save the Event list in SharedPreferences
    public static void saveEvents(Context context, List<Event> events) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(events);
        editor.putString(KEY_EVENT, json);
        editor.apply();
    }

    // Method to retrieve the Event list of Event from SharedPreferences
    public static List<Event> getEvents(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_EVENT, null);
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
