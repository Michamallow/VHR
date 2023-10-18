package com.example.vhr.Calendar;

import static com.example.vhr.DoctorActivity.DoctorPreferencesSave.getDoctors;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vhr.DoctorActivity.Doctor;
import com.example.vhr.DoctorActivity.DoctorPreferencesSave;
import com.example.vhr.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateDetailActivity extends AppCompatActivity {

    public static final String EXTRA_DATE = "extra_date";

    private List<Event> events = new ArrayList<>();
    private Spinner timeSpinner;
    private Spinner doctorSpinner;

    private List<Doctor> doctorsList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_detail);

        timeSpinner = findViewById(R.id.timeSpinner);
        doctorSpinner = findViewById(R.id.doctorSpinner);

        doctorsList = DoctorPreferencesSave.getDoctors(this);
        if (doctorsList == null) {
            doctorsList = new ArrayList<>();
        }

        List<String> nameDoctorList = new ArrayList<>();
        for (Doctor doctor : doctorsList) {
            nameDoctorList.add(doctor.getNom());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nameDoctorList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(adapter);

        Date selectedDate = (Date) getIntent().getSerializableExtra(EXTRA_DATE);

        List<Event> eventsForDate = (List<Event>) getIntent().getSerializableExtra("eventsForDate");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(selectedDate);

        TextView dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText("Selected Date: " + formattedDate);

        TextView textView = findViewById(R.id.textView);
        if (eventsForDate.isEmpty()) {
            textView.setText("You have no event");
        } else {
            StringBuilder eventText = new StringBuilder("Events for this date:\n");
            for (Event event : eventsForDate) {
                String boldName = event.getName();
                SpannableStringBuilder sb = new SpannableStringBuilder(boldName);
                sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, boldName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                eventText.append(sb).append(" ").append(formatTime(event.getDateTime())).append("\n");

                eventText.append(event.getDescription()).append("\n");

                eventText.append("Doctor: ").append(event.getDoctor().getNom()).append("\n\n");
            }
            textView.setText(eventText.toString());
        }

        EditText eventNameEditText = findViewById(R.id.eventNameEditText);
        EditText eventDescriptionEditText = findViewById(R.id.eventDescriptionEditText);
        Button saveEventButton = findViewById(R.id.addEvent);
        Button backButton = findViewById(R.id.back);

        saveEventButton.setOnClickListener(v -> saveEvent(selectedDate, eventNameEditText, eventDescriptionEditText, timeSpinner, doctorSpinner));

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });

    }

    private void saveEvent(Date selectedDate, EditText eventNameEditText, EditText eventDescriptionEditText, Spinner spinnerTime, Spinner spinnerDoctor) {
        String eventName = eventNameEditText.getText().toString().trim();
        String eventDescription = eventDescriptionEditText.getText().toString().trim();

        if (!eventName.isEmpty()) {
            String selectedTime = spinnerTime.getSelectedItem().toString();

            String[] timeComponents = selectedTime.split(":");
            int selectedHour = Integer.parseInt(timeComponents[0]);
            int selectedMinute = Integer.parseInt(timeComponents[1].split(" ")[0]);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);

            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);

            Date updatedDate = calendar.getTime();

            String selectedDoctorName = spinnerDoctor.getSelectedItem().toString();
            Doctor selectedDoctor = findDoctorByName(selectedDoctorName);
            Event event;

            if(selectedDoctor != null)
                event = new Event(updatedDate, eventName, eventDescription, selectedDoctor);
            else
                event = new Event(updatedDate, eventName, eventDescription, null);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("event_name", eventName);
            resultIntent.putExtra("event", event);

            setResult(RESULT_OK, resultIntent);
            events = EventSave.getEvents(this);
            if(events == null){
                events = new ArrayList<>();
            }
            events.add(event);
            EventSave.saveEvents(this,events);
            finish();
        } else {
            Log.e("DateDetailActivity", "Event name is empty");
        }
    }

    private String formatTime(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(dateTime);
    }

    private Doctor findDoctorByName(String doctorName) {
        for (Doctor doctor : doctorsList) {
            if (doctor.getNom().equals(doctorName)) {
                return doctor;
            }
        }
        return null;
    }

}
