package com.example.traveladvisor360.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.TripActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private List<TripActivity> activities;

    public ActivityAdapter(List<TripActivity> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        TripActivity activity = activities.get(position);
        holder.activityNameText.setText(activity.getName());
        holder.activityIconView.setImageResource(activity.getIconResId());

        // Update selection state
        if (activity.isSelected()) {
            holder.activityCard.setStrokeColor(holder.itemView.getContext().getResources().getColor(R.color.colorAccent));
            holder.activityCard.setStrokeWidth(4);
            holder.activityCard.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.colorAccent));
        } else {
            holder.activityCard.setStrokeColor(holder.itemView.getContext().getResources().getColor(R.color.gray_light));
            holder.activityCard.setStrokeWidth(1);
            holder.activityCard.setCardBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }

        // Set click listener
        holder.activityCard.setOnClickListener(v -> {
            activity.setSelected(!activity.isSelected());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView activityCard;
        TextView activityNameText;
        ImageView activityIconView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            activityCard = itemView.findViewById(R.id.card_activity);
            activityNameText = itemView.findViewById(R.id.text_activity_name);
            activityIconView = itemView.findViewById(R.id.image_activity_icon);
        }
    }
}