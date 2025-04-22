package com.example.traveladvisor360.adapters;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Destination;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private Context context;
    private List<Destination> destinations = new ArrayList<>();
    private OnDestinationClickListener listener;

    public DestinationAdapter(Context context) {
        this.context = context;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
        notifyDataSetChanged();
    }

    public void setOnDestinationClickListener(OnDestinationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinations.get(position);

        // Set destination data
        holder.tvName.setText(destination.getName());
        holder.tvLocation.setText(destination.getCountry());
        holder.tvDescription.setText(destination.getDescription());
        holder.ratingBar.setRating(destination.getRating());
        holder.tvRating.setText(String.format("%.1f", destination.getRating()));
        holder.tvReviews.setText(String.format("(%d reviews)", destination.getReviewCount()));

        // Load image
        Glide.with(context)
                .load(getImageResource(destination.getImageUrl()))
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.ivDestination);

        // Set tags
        setupTags(holder.tagsContainer, destination.getTags());

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDestinationClick(destination);
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

    private void setupTags(LinearLayout container, List<String> tags) {
        container.removeAllViews();
        for (String tag : tags) {
            TextView tagView = (TextView) LayoutInflater.from(context)
                    .inflate(R.layout.item_tag, container, false);
            tagView.setText(tag);
            container.addView(tagView);
        }
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public interface OnDestinationClickListener {
        void onDestinationClick(Destination destination);
    }

    static class DestinationViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView ivDestination;
        TextView tvName;
        TextView tvLocation;
        TextView tvDescription;
        RatingBar ratingBar;
        TextView tvRating;
        TextView tvReviews;
        LinearLayout tagsContainer;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_destination);
            ivDestination = itemView.findViewById(R.id.iv_destination);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvReviews = itemView.findViewById(R.id.tv_reviews);
            tagsContainer = itemView.findViewById(R.id.tags_container);
        }
    }
}