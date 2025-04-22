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
import com.example.traveladvisor360.models.Itinerary;
import com.example.traveladvisor360.models.ItineraryDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder> {

    private Context context;
    private List<Itinerary> itineraries = new ArrayList<>();
    private List<ItineraryDay> itineraryDays = new ArrayList<>();
    private OnItineraryClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    private boolean isShowingItineraryDays = false;

    public ItineraryAdapter(Context context) {
        this.context = context;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
        this.isShowingItineraryDays = false;
        notifyDataSetChanged();
    }

    public void setItineraryDays(List<ItineraryDay> itineraryDays) {
        this.itineraryDays = itineraryDays;
        this.isShowingItineraryDays = true;
        notifyDataSetChanged();
    }

    public void setOnItineraryClickListener(OnItineraryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isShowingItineraryDays) {
            view = LayoutInflater.from(context).inflate(R.layout.item_itinerary_day, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_itinerary, parent, false);
        }
        return new ItineraryViewHolder(view, isShowingItineraryDays);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryViewHolder holder, int position) {
        if (isShowingItineraryDays) {
            ItineraryDay day = itineraryDays.get(position);
            bindItineraryDayView(holder, day);
        } else {
            Itinerary itinerary = itineraries.get(position);
            bindItineraryView(holder, itinerary);
        }
    }

    private void bindItineraryView(ItineraryViewHolder holder, Itinerary itinerary) {
        // Bind normal itinerary view
        holder.tvTitle.setText(itinerary.getTitle());
        holder.tvDestination.setText(itinerary.getDestination());
        holder.tvDates.setText(String.format("%s - %s",
                dateFormat.format(itinerary.getStartDate()),
                dateFormat.format(itinerary.getEndDate())));
        holder.tvDuration.setText(String.format(Locale.getDefault(), "%d days",
                calculateDuration(itinerary.getStartDate(), itinerary.getEndDate())));
        holder.tvBudget.setText(String.format(Locale.getDefault(), "$%.0f", itinerary.getBudget()));

        if (itinerary.isCollaborative()) {
            holder.ivCollaborative.setVisibility(View.VISIBLE);
        } else {
            holder.ivCollaborative.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItineraryClick(itinerary);
            }
        });
    }

    private void bindItineraryDayView(ItineraryViewHolder holder, ItineraryDay day) {
        // Bind itinerary day view
        holder.tvDayTitle.setText(String.format("Day %d - %s", day.getDayNumber(), day.getFormattedDate()));

        // Set day notes if available
        if (day.getNotes() != null && !day.getNotes().isEmpty()) {
            holder.tvDayNotes.setText(day.getNotes());
            holder.tvDayNotes.setVisibility(View.VISIBLE);
        } else {
            holder.tvDayNotes.setVisibility(View.GONE);
        }

        // Set up activities recycler view
        if (holder.rvActivities != null) {
            holder.rvActivities.setLayoutManager(new LinearLayoutManager(context));
            ItineraryActivityAdapter activityAdapter = new ItineraryActivityAdapter(context);
            holder.rvActivities.setAdapter(activityAdapter);

            // Show activities or empty state
            if (day.getActivities() != null && !day.getActivities().isEmpty()) {
                activityAdapter.setActivities(day.getActivities());
                holder.rvActivities.setVisibility(View.VISIBLE);
                if (holder.tvNoActivities != null) {
                    holder.tvNoActivities.setVisibility(View.GONE);
                }
            } else {
                holder.rvActivities.setVisibility(View.GONE);
                if (holder.tvNoActivities != null) {
                    holder.tvNoActivities.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private int calculateDuration(java.util.Date startDate, java.util.Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24)) + 1;
    }

    @Override
    public int getItemCount() {
        return isShowingItineraryDays ? itineraryDays.size() : itineraries.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isShowingItineraryDays ? 1 : 0;
    }

    public interface OnItineraryClickListener {
        void onItineraryClick(Itinerary itinerary);
    }

    static class ItineraryViewHolder extends RecyclerView.ViewHolder {
        // Itinerary list view
        TextView tvTitle;
        TextView tvDestination;
        TextView tvDates;
        TextView tvDuration;
        TextView tvBudget;
        View ivCollaborative;

        // Itinerary day view
        TextView tvDayTitle;
        TextView tvDayNotes;
        RecyclerView rvActivities;
        TextView tvNoActivities;

        public ItineraryViewHolder(@NonNull View itemView, boolean isItineraryDay) {
            super(itemView);
            if (isItineraryDay) {
                // Initialize day view
                tvDayTitle = itemView.findViewById(R.id.tv_day_title);
                tvDayNotes = itemView.findViewById(R.id.tv_day_notes);
                rvActivities = itemView.findViewById(R.id.rv_activities);
                tvNoActivities = itemView.findViewById(R.id.tv_no_activities);
            } else {
                // Initialize regular itinerary view
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvDestination = itemView.findViewById(R.id.tv_destination);
                tvDates = itemView.findViewById(R.id.tv_dates);
                tvDuration = itemView.findViewById(R.id.tv_duration);
                tvBudget = itemView.findViewById(R.id.tv_budget);
                ivCollaborative = itemView.findViewById(R.id.iv_collaborative);
            }
        }
    }
}