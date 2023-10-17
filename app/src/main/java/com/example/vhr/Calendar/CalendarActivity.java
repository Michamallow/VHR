package com.example.vhr.Calendar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vhr.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_EVENT = 1;

    private List<Event> events = new ArrayList<>();

    private Date selectedDate;
    private EventListAdapter eventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.calendarView);
        ListView eventListView = findViewById(R.id.eventListView);
        events = EventSave.getEvents(this);
        if(events == null){
            events = new ArrayList<>();
        }
        eventListAdapter = new EventListAdapter(this, R.layout.event_list_item, events);
        eventListView.setAdapter(eventListAdapter);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = getDate(year, month, dayOfMonth);
            showEventsForDate();
            openDateDetailActivity(selectedDate);
        });
        showEventsForDate();

        eventListView.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = events.get(position);
            openDateDetailActivity(selectedEvent.getDateTime());
        });

        eventListView.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmationDialog(position);
            return true;
        });
        createNotificationChannel();

    }

    private void showEventsForDate() {
        Collections.sort(events, Comparator.comparing(Event::getDateTime));

        for (Event event : events) {
            Calendar calEvent = Calendar.getInstance();
            calEvent.setTime(event.getDateTime());

            Calendar tomorrowCal = Calendar.getInstance();
            tomorrowCal.add(Calendar.DAY_OF_MONTH, 1);

            if (calEvent.get(Calendar.YEAR) == tomorrowCal.get(Calendar.YEAR) &&
                    calEvent.get(Calendar.MONTH) == tomorrowCal.get(Calendar.MONTH) &&
                    calEvent.get(Calendar.DAY_OF_MONTH) == tomorrowCal.get(Calendar.DAY_OF_MONTH)) {

                showPopup(event.getName());
            }
        }

        eventListAdapter.notifyDataSetChanged();
    }

    private Date getDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTime();
    }

    private void openDateDetailActivity(Date date) {
        List<Event> eventsForDate = getEventsForDate(date);

        Intent intent = new Intent(this, DateDetailActivity.class);
        intent.putExtra(DateDetailActivity.EXTRA_DATE, date);
        intent.putExtra("eventsForDate", new ArrayList<>(eventsForDate));
        startActivityForResult(intent, REQUEST_CODE_ADD_EVENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_EVENT && resultCode == DateDetailActivity.RESULT_OK && data != null) {
            if (data.hasExtra("event_name") && data.hasExtra("event")) {
                Event newEvent = (Event) data.getSerializableExtra("event");

                assert newEvent != null;
                Toast.makeText(this, "Event added: " + newEvent.getName(), Toast.LENGTH_SHORT).show();

                events.add(newEvent);
                showEventsForDate();

                data.putExtra("events", new ArrayList<>(events));

                Calendar tomorrowCal = Calendar.getInstance();
                tomorrowCal.add(Calendar.DAY_OF_MONTH, 1);

                Calendar eventCal = Calendar.getInstance();
                eventCal.setTime(newEvent.getDateTime());

                if (eventCal.get(Calendar.YEAR) == tomorrowCal.get(Calendar.YEAR) &&
                        eventCal.get(Calendar.MONTH) == tomorrowCal.get(Calendar.MONTH) &&
                        eventCal.get(Calendar.DAY_OF_MONTH) == tomorrowCal.get(Calendar.DAY_OF_MONTH)) {
                }

            } else {
                Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_CODE_ADD_EVENT && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Adding event canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Event> getEventsForDate(Date date) {
        List<Event> eventsForDate = new ArrayList<>();
        for (Event event : events) {
            Calendar calEvent = Calendar.getInstance();
            calEvent.setTime(event.getDateTime());

            Calendar calDate = Calendar.getInstance();
            calDate.setTime(date);

            if (calEvent.get(Calendar.YEAR) == calDate.get(Calendar.YEAR) &&
                    calEvent.get(Calendar.MONTH) == calDate.get(Calendar.MONTH) &&
                    calEvent.get(Calendar.DAY_OF_MONTH) == calDate.get(Calendar.DAY_OF_MONTH)) {
                eventsForDate.add(event);
            }
        }
        return eventsForDate;
    }

    private void showDeleteConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete this event?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            deleteEvent(position);
        });
        builder.setNegativeButton("No", null);

        builder.create().show();
    }

    private void deleteEvent(int position) {
        if (position >= 0 && position < events.size()) {
            Event deletedEvent = events.remove(position);
            showEventsForDate();
            Toast.makeText(this, "Event deleted: " + deletedEvent.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channelId", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showPopup(String eventName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reminder for tomorrow");
        builder.setMessage("You have an appointment tomorrow: " + eventName);
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }
}
