package com.example.traveladvisor360.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.example.traveladvisor360.models.ItineraryActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;

import java.util.ArrayList;
import java.util.List;

public class ItineraryActivityAdapter extends RecyclerView.Adapter<ItineraryActivityAdapter.ActivityViewHolder> {

    private Context context;
    private List<ItineraryActivity> activities = new ArrayList<>();

    public ItineraryActivityAdapter(Context context) {
        this.context = context;
    }

    public void setActivities(List<ItineraryActivity> activities) {
        this.activities = activities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout .item_itinerary_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ItineraryActivity activity = activities.get(position);

        // Set activity icon based on type
        holder.ivActivityType.setImageResource(activity.getTypeIcon());

        // Set activity time
        holder.tvActivityTime.setText(activity.getFormattedTime());

        // Set activity title
        holder.tvActivityTitle.setText(activity.getTitle());

        // Set location if available
        if (activity.getLocation() != null && !activity.getLocation().isEmpty()) {
            holder.tvActivityLocation.setText(activity.getLocation());
            holder.tvActivityLocation.setVisibility(View.VISIBLE);
        } else {
            holder.tvActivityLocation.setVisibility(View.GONE);
        }

        // Set cost if available
        String cost = activity.getFormattedCost();
        if (!cost.isEmpty()) {
            holder.tvActivityCost.setText(cost);
            holder.tvActivityCost.setVisibility(View.VISIBLE);
        } else {
            holder.tvActivityCost.setVisibility(View.GONE);
        }

        // Set notes if available
        if (activity.getNotes() != null && !activity.getNotes().isEmpty()) {
            holder.tvActivityNotes.setText(activity.getNotes());
            holder.tvActivityNotes.setVisibility(View.VISIBLE);
        } else {
            holder.tvActivityNotes.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        ImageView ivActivityType;
        TextView tvActivityTime;
        TextView tvActivityTitle;
        TextView tvActivityLocation;
        TextView tvActivityCost;
        TextView tvActivityNotes;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            ivActivityType = itemView.findViewById(R.id.iv_activity_type);
            tvActivityTime = itemView.findViewById(R.id.tv_activity_time);
            tvActivityTitle = itemView.findViewById(R.id.tv_activity_title);
            tvActivityLocation = itemView.findViewById(R.id.tv_activity_location);
            tvActivityCost = itemView.findViewById(R.id.tv_activity_cost);
            tvActivityNotes = itemView.findViewById(R.id.tv_activity_notes);
        }
    }
}