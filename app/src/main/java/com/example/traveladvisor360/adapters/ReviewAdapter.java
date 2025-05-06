package com.example.traveladvisor360.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context) {
        this.context = context;
        this.reviews = new ArrayList<>();
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.tvReviewerName.setText(review.getUserName());
        holder.tvReviewDate.setText(review.getFormattedDate());
        holder.tvReviewText.setText(review.getText());
        holder.ratingBar.setRating((float) review.getRating());

        // Load reviewer profile image if available
        if (review.getUserProfileImageUrl() != null && !review.getUserProfileImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(review.getUserProfileImageUrl())
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(holder.ivReviewerProfile);
        } else {
            holder.ivReviewerProfile.setImageResource(R.drawable.ic_profile);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivReviewerProfile;
        TextView tvReviewerName;
        TextView tvReviewDate;
        RatingBar ratingBar;
        TextView tvReviewText;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivReviewerProfile = itemView.findViewById(R.id.iv_reviewer_profile);
            tvReviewerName = itemView.findViewById(R.id.tv_reviewer_name);
            tvReviewDate = itemView.findViewById(R.id.tv_review_date);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            tvReviewText = itemView.findViewById(R.id.tv_review_text);
        }
    }
}
