package com.example.traveladvisor360.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.interfaces.ExperienceInteractionListener;
import com.example.traveladvisor360.models.Comment;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.utils.TimeUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExperienceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View types
    private static final int VIEW_TYPE_EXPERIENCE = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private final Context context;
    private List<Experience> experiences;
    private ExperienceInteractionListener listener;
    private boolean isLoading = false;

    // For the grid view in ExperienceFragment
    private boolean isGridView = false;

    public ExperienceAdapter(Context context) {
        this.context = context;
        this.experiences = new ArrayList<>();
    }

    public ExperienceAdapter(Context context, ExperienceInteractionListener listener) {
        this.context = context;
        this.listener = listener;
        this.experiences = new ArrayList<>();
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
        notifyDataSetChanged();
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyDataSetChanged();
    }

    public void setGridView(boolean gridView) {
        isGridView = gridView;
    }

    public void setOnExperienceClickListener(ExperienceInteractionListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoading && position == experiences.size()) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_EXPERIENCE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        } else {
            View view;
            if (isGridView) {
                view = LayoutInflater.from(context).inflate(R.layout.item_experience_grid, parent, false);
                return new GridExperienceViewHolder(view);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.item_experience, parent, false);
                return new ExperienceViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder.getItemViewType() == VIEW_TYPE_EXPERIENCE) {
                Experience experience = experiences.get(position);
                if (isGridView) {
                    bindGridExperienceViewHolder((GridExperienceViewHolder) holder, experience);
                } else {
                    bindExperienceViewHolder((ExperienceViewHolder) holder, experience);
                }
            }
            // No need to bind anything for loading view holder
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindExperienceViewHolder(ExperienceViewHolder holder, Experience experience) {
        try {
            // Bind user info if available
            if (holder.userInfoContainer != null) {
                if (experience.getUserName() != null && !experience.getUserName().isEmpty()) {
                    if (holder.tvUserName != null) {
                        holder.tvUserName.setText(experience.getUserName());
                    }

                    if (experience.getCreatedAt() != null && holder.tvPostTime != null) {
                        holder.tvPostTime.setText(TimeUtils.getTimeAgo(experience.getCreatedAt()));
                    }

                    if (experience.getUserPhotoUrl() != null && !experience.getUserPhotoUrl().isEmpty() && holder.ivUserPhoto != null) {
                        Glide.with(context)
                                .load(experience.getUserPhotoUrl())
                                .placeholder(R.drawable.placeholder_profile)
                                .error(R.drawable.placeholder_profile)
                                .into(holder.ivUserPhoto);
                    }

                    holder.userInfoContainer.setVisibility(View.VISIBLE);
                } else {
                    // Hide user info section if not available
                    holder.userInfoContainer.setVisibility(View.GONE);
                }
            }

            // Bind experience details
            if (holder.tvTitle != null && experience.getTitle() != null) {
                holder.tvTitle.setText(experience.getTitle());
            }

            if (holder.tvDescription != null && experience.getDescription() != null) {
                holder.tvDescription.setText(experience.getDescription());
            }

            if (holder.tvLocation != null && experience.getLocation() != null) {
                holder.tvLocation.setText(experience.getLocation());
            }

            if (holder.tvRating != null) {
                holder.tvRating.setText(String.format(Locale.getDefault(), "%.1f", experience.getRating()));
            }

            if (holder.tvReviewCount != null) {
                if (experience.getReviewCount() > 0) {
                    holder.tvReviewCount.setText(String.format(Locale.getDefault(), "(%d)", experience.getReviewCount()));
                    holder.tvReviewCount.setVisibility(View.VISIBLE);
                } else {
                    holder.tvReviewCount.setVisibility(View.GONE);
                }
            }

            // If duration is in float hours, convert to a readable string
            if (holder.tvDuration != null) {
                holder.tvDuration.setText(experience.getDurationString());
            }

            // Format price with currency
            if (holder.tvPrice != null) {
                holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.0f", experience.getPrice()));
            }

            // Set category
            if (holder.chipCategory != null) {
                if (experience.getCategory() != null && !experience.getCategory().isEmpty()) {
                    holder.chipCategory.setText(experience.getCategory());
                    holder.chipCategory.setVisibility(View.VISIBLE);
                } else {
                    holder.chipCategory.setVisibility(View.GONE);
                }
            }

            // Featured badge visibility
            if (holder.chipFeatured != null) {
                holder.chipFeatured.setVisibility(experience.isFeatured() ? View.VISIBLE : View.GONE);
            }

            // Load image with Glide
            if (holder.ivExperience != null && experience.getImageUrl() != null && !experience.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(experience.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .centerCrop()
                        .into(holder.ivExperience);
            }

            // Social interaction elements
            if (holder.btnLike != null) {
                updateLikeButton(holder, experience);
            }

            if (holder.tvLikeCount != null) {
                holder.tvLikeCount.setText(String.format(Locale.getDefault(), "%d likes", experience.getLikeCount()));
            }

            // Comments preview
            if (experience.getComments() != null && !experience.getComments().isEmpty()) {
                if (holder.tvCommentUsername1 != null && holder.tvCommentContent1 != null && holder.commentPreview1 != null) {
                    Comment firstComment = experience.getComments().get(0);
                    holder.tvCommentUsername1.setText(firstComment.getUserName());
                    holder.tvCommentContent1.setText(firstComment.getContent());
                    holder.commentPreview1.setVisibility(View.VISIBLE);
                }

                // Set comment count
                if (holder.tvViewAllComments != null) {
                    holder.tvViewAllComments.setText(String.format(Locale.getDefault(),
                            "View all %d comments", experience.getComments().size()));
                    holder.tvViewAllComments.setVisibility(View.VISIBLE);
                }
            } else {
                if (holder.commentPreview1 != null) {
                    holder.commentPreview1.setVisibility(View.GONE);
                }
                if (holder.tvViewAllComments != null) {
                    holder.tvViewAllComments.setVisibility(View.GONE);
                }
            }

            // Current user profile photo for comment section
            if (holder.ivCurrentUser != null) {
                Glide.with(context)
                        .load("https://randomuser.me/api/portraits/women/17.jpg") // Current user photo
                        .placeholder(R.drawable.placeholder_profile)
                        .error(R.drawable.placeholder_profile)
                        .into(holder.ivCurrentUser);
            }

            // Set click listeners
            if (holder.cardExperience != null) {
                holder.cardExperience.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onExperienceClick(experience);
                    }
                });
            }

            if (holder.btnLike != null) {
                holder.btnLike.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onLikeClick(experience);
                        // Update UI immediately
                        updateLikeButton(holder, experience);
                        if (holder.tvLikeCount != null) {
                            holder.tvLikeCount.setText(String.format(Locale.getDefault(), "%d likes", experience.getLikeCount()));
                        }
                    }
                });
            }

            if (holder.btnComment != null) {
                holder.btnComment.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onCommentClick(experience);
                    }
                });
            }

            if (holder.btnShare != null) {
                holder.btnShare.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onShareClick(experience);
                    }
                });
            }

            if (holder.btnSave != null) {
                holder.btnSave.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onBookmarkClick(experience);
                    }
                });
            }

            if (holder.btnBookNow != null) {
                holder.btnBookNow.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onBookNowClick(experience);
                    }
                });
            }

            // Post comment functionality
            if (holder.btnPostComment != null && holder.etAddComment != null) {
                holder.btnPostComment.setOnClickListener(v -> {
                    String commentText = holder.etAddComment.getText().toString().trim();
                    if (!TextUtils.isEmpty(commentText)) {
                        // In a real app, you would send this to your backend
                        // For demo, we'll just clear the input
                        holder.etAddComment.setText("");

                        // Show confirmation
                        if (listener != null) {
                            listener.onCommentClick(experience);
                        }
                    }
                });
            }

            // "View more" for description
            if (holder.tvViewMore != null && holder.tvDescription != null) {
                holder.tvViewMore.setOnClickListener(v -> {
                    if (holder.tvDescription.getMaxLines() == 3) {
                        holder.tvDescription.setMaxLines(Integer.MAX_VALUE);
                        holder.tvDescription.setEllipsize(null);
                        holder.tvViewMore.setText("Show less");
                    } else {
                        holder.tvDescription.setMaxLines(3);
                        holder.tvDescription.setEllipsize(TextUtils.TruncateAt.END);
                        holder.tvViewMore.setText("View more");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindGridExperienceViewHolder(GridExperienceViewHolder holder, Experience experience) {
        try {
            // Set basic experience info
            if (holder.tvTitle != null && experience.getTitle() != null) {
                holder.tvTitle.setText(experience.getTitle());
            }

            if (holder.tvLocation != null && experience.getLocation() != null) {
                holder.tvLocation.setText(experience.getLocation());
            }

            if (holder.tvRating != null) {
                holder.tvRating.setText(String.format(Locale.getDefault(), "%.1f", experience.getRating()));
            }

            if (holder.tvPrice != null) {
                holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.0f", experience.getPrice()));
            }

            // Set category chip
            if (holder.chipCategory != null) {
                if (experience.getCategory() != null && !experience.getCategory().isEmpty()) {
                    holder.chipCategory.setText(experience.getCategory());
                    holder.chipCategory.setVisibility(View.VISIBLE);
                } else {
                    holder.chipCategory.setVisibility(View.GONE);
                }
            }

            // Load image
            if (holder.ivExperience != null && experience.getImageUrl() != null && !experience.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(experience.getImageUrl())
                        .placeholder(R.drawable.placeholder_experience)
                        .error(R.drawable.placeholder_experience)
                        .centerCrop()
                        .into(holder.ivExperience);
            }

            // Set click listener
            if (holder.cardExperience != null) {
                holder.cardExperience.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onExperienceClick(experience);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLikeButton(ExperienceViewHolder holder, Experience experience) {
        try {
            if (holder.btnLike != null) {
                if (experience.isLiked()) {
                    holder.btnLike.setImageResource(R.drawable.ic_heart_filled);
                    holder.btnLike.setColorFilter(context.getResources().getColor(R.color.like_active, null));
                } else {
                    holder.btnLike.setImageResource(R.drawable.ic_heart_outliine);
                    holder.btnLike.setColorFilter(context.getResources().getColor(R.color.primary_text, null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return experiences.size() + (isLoading ? 1 : 0);
    }

    // View holder for the experiences in feed view
    static class ExperienceViewHolder extends RecyclerView.ViewHolder {
        // User info
        View userInfoContainer;
        CircleImageView ivUserPhoto;
        TextView tvUserName, tvPostTime;
        ImageButton btnMoreOptions;

        // Experience card
        MaterialCardView cardExperience;
        ImageView ivExperience;
        Chip chipCategory, chipFeatured;

        // Action buttons
        ImageButton btnLike, btnComment, btnSave, btnShare;
        TextView tvLikeCount;

        // Experience details
        TextView tvTitle, tvDescription, tvLocation, tvRating, tvReviewCount, tvDuration, tvPrice, tvViewMore;

        // Comments section
        TextView tvViewAllComments, tvCommentUsername1, tvCommentContent1;
        LinearLayout commentPreview1;
        CircleImageView ivCurrentUser;
        EditText etAddComment;
        TextView btnPostComment;

        // Book now button
        MaterialButton btnBookNow;

        public ExperienceViewHolder(@NonNull View itemView) {
            super(itemView);

            // User info
            userInfoContainer = itemView.findViewById(R.id.user_info_container);
            ivUserPhoto = itemView.findViewById(R.id.iv_user_photo);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvPostTime = itemView.findViewById(R.id.tv_post_time);
            btnMoreOptions = itemView.findViewById(R.id.btn_more_options);

            // Experience card
            cardExperience = itemView.findViewById(R.id.card_experience);
            ivExperience = itemView.findViewById(R.id.iv_experience);
            chipCategory = itemView.findViewById(R.id.chip_category);
            chipFeatured = itemView.findViewById(R.id.chip_featured);

            // Action buttons
            btnLike = itemView.findViewById(R.id.btn_like);
            btnComment = itemView.findViewById(R.id.btn_comment);
            btnSave = itemView.findViewById(R.id.btn_save);
            btnShare = itemView.findViewById(R.id.btn_share);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);

            // Experience details
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvReviewCount = itemView.findViewById(R.id.tv_review_count);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvViewMore = itemView.findViewById(R.id.tv_view_more);

            // Comments section
            tvViewAllComments = itemView.findViewById(R.id.tv_view_all_comments);
            tvCommentUsername1 = itemView.findViewById(R.id.tv_comment_username_1);
            tvCommentContent1 = itemView.findViewById(R.id.tv_comment_content_1);
            commentPreview1 = itemView.findViewById(R.id.comment_preview_1);
            ivCurrentUser = itemView.findViewById(R.id.iv_current_user);
            etAddComment = itemView.findViewById(R.id.et_add_comment);
            btnPostComment = itemView.findViewById(R.id.btn_post_comment);

            // Book now button
            btnBookNow = itemView.findViewById(R.id.btn_book_now);
        }
    }

    // View holder for the grid view experiences
    static class GridExperienceViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardExperience;
        ImageView ivExperience;
        TextView tvTitle, tvLocation, tvRating, tvPrice;
        Chip chipCategory;

        public GridExperienceViewHolder(@NonNull View itemView) {
            super(itemView);
            cardExperience = itemView.findViewById(R.id.card_experience);
            ivExperience = itemView.findViewById(R.id.iv_experience);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvPrice = itemView.findViewById(R.id.tv_price);
            chipCategory = itemView.findViewById(R.id.chip_category);
        }
    }

    // View holder for the loading indicator at the bottom of the list
    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }
}