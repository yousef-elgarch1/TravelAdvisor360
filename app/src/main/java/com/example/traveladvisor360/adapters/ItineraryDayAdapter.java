package com.example.traveladvisor360.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.ItineraryDay;

import java.util.ArrayList;
import java.util.List;

public class ItineraryDayAdapter extends RecyclerView.Adapter<ItineraryDayAdapter.DayViewHolder> {

    private Context context;
    private List<ItineraryDay> days = new ArrayList<>();

    public ItineraryDayAdapter(Context context) {
        this.context = context;
    }

    public void setDays(List<ItineraryDay> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_itinerary_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        ItineraryDay day = days.get(position);

        // Set day title
        holder.tvDayTitle.setText(String.format("Day %d - %s", day.getDayNumber(), day.getFormattedDate()));

        // Set day notes if available
        if (day.getNotes() != null && !day.getNotes().isEmpty()) {
            holder.tvDayNotes.setText(day.getNotes());
            holder.tvDayNotes.setVisibility(View.VISIBLE);
        } else {
            holder.tvDayNotes.setVisibility(View.GONE);
        }

        // Set up activities recycler view
        holder.rvActivities.setLayoutManager(new LinearLayoutManager(context));
        ItineraryActivityAdapter activityAdapter = new ItineraryActivityAdapter(context);
        holder.rvActivities.setAdapter(activityAdapter);

        // Show activities or empty state
        if (day.getActivities() != null && !day.getActivities().isEmpty()) {
            activityAdapter.setActivities(day.getActivities());
            holder.rvActivities.setVisibility(View.VISIBLE);
            holder.tvNoActivities.setVisibility(View.GONE);
        } else {
            holder.rvActivities.setVisibility(View.GONE);
            holder.tvNoActivities.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayTitle;
        TextView tvDayNotes;
        RecyclerView rvActivities;
        TextView tvNoActivities;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayTitle = itemView.findViewById(R.id.tv_day_title);
            tvDayNotes = itemView.findViewById(R.id.tv_day_notes);
            rvActivities = itemView.findViewById(R.id.rv_activities);
            tvNoActivities = itemView.findViewById(R.id.tv_no_activities);
        }
    }
}