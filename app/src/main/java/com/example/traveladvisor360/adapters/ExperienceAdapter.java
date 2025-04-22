package com.example.traveladvisor360.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Experience;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder> {

    private Context context;
    private List<Experience> experiences = new ArrayList<>();
    private OnExperienceClickListener listener;

    public ExperienceAdapter(Context context) {
        this.context = context;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
        notifyDataSetChanged();
    }

    public void setOnExperienceClickListener(OnExperienceClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_experience, parent, false);
        return new ExperienceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder holder, int position) {
        Experience experience = experiences.get(position);

        // Set experience data
        holder.tvTitle.setText(experience.getTitle());
        holder.tvLocation.setText(experience.getLocation());
        holder.tvRating.setText(String.format(Locale.getDefault(), "%.1f", experience.getRating()));
        holder.chipCategory.setText(experience.getCategory());
        holder.tvDuration.setText(String.format(Locale.getDefault(), "%.1f hours", experience.getDuration()));
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.0f", experience.getPrice()));

        // Load image
        Glide.with(context)
                .load(getImageResource(experience.getImageUrl()))
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.ivExperience);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onExperienceClick(experience);
            }
        });
    }

    private int getImageResource(String imageName) {
        // In a real app, this would come from a URL
        int resourceId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());

        // Return placeholder if image not found
        return resourceId != 0 ? resourceId : R.drawable.placeholder_image;
    }

    @Override
    public int getItemCount() {
        return experiences.size();
    }

    public interface OnExperienceClickListener {
        void onExperienceClick(Experience experience);
    }

    static class ExperienceViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView ivExperience;
        TextView tvTitle;
        TextView tvLocation;
        TextView tvRating;
        Chip chipCategory;
        TextView tvDuration;
        TextView tvPrice;

        public ExperienceViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_experience);
            ivExperience = itemView.findViewById(R.id.iv_experience);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvRating = itemView.findViewById(R.id.tv_rating);
            chipCategory = itemView.findViewById(R.id.chip_category);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}