package hgcq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hgcq.model.dto.EventDTO;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {

    private Context context;
    private List<EventDTO> eventList;

    public EventAdapter(Context context, List<EventDTO> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    public static class EventHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView date;

        public EventHolder(View view) {
            super(view);
//            name = view.findViewById(R.id.eventName);
//            date = view.findViewById(R.id.eventDate);
        }
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View eventView = LayoutInflater.from(parent.getContext()).inflate("R.layout.", parent, false);
//        return new EventHolder(eventView);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        EventDTO event = eventList.get(position);
        holder.name.setText(event.getName());
        holder.date.setText(event.getDate());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
