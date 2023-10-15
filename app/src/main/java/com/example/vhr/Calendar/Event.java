package com.example.vhr.Calendar;

import androidx.annotation.Nullable;

import com.example.vhr.DoctorActivity.Doctor;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {
    private Date dateTime;
    private String name;
    private String description;
    @Nullable
    private Doctor doctor;

    public Event(Date dateTime, String name, String description, Doctor doctor) {
        this.dateTime = dateTime;
        this.name = name;
        this.description = description;
        this.doctor = doctor;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    @Override
    public String toString() {
        return name;
    }
}
