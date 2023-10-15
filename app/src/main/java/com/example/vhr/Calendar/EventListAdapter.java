package com.example.vhr.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vhr.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventListAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final int resource;
    private final List<Event> events;

    public EventListAdapter(@NonNull Context context, int resource, @NonNull List<Event> events) {
        super(context, resource, events);
        this.context = context;
        this.resource = resource;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
        }

        Event event = events.get(position);

        TextView eventTextView = convertView.findViewById(R.id.eventTextView);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(event.getDateTime());
        String eventText = String.format("%s: %s, %s", formattedDate, event.getName(), event.getDescription());
        eventTextView.setText(eventText);

        return convertView;
    }
}
