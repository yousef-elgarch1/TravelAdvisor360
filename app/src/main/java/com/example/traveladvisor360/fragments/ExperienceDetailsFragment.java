package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.CommentAdapter;
import com.example.traveladvisor360.adapters.ReviewAdapter;
import com.example.traveladvisor360.models.Comment;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.utils.ApiClient;
import com.example.traveladvisor360.utils.TimeUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExperienceDetailsFragment extends Fragment {

    private ImageView ivExperienceImage;
    private CircleImageView ivUserPhoto;
    private TextView tvUserName;
    private TextView tvPostTime;
    private TextView tvExperienceTitle;
    private TextView tvExperienceDescription;
    private TextView tvExperienceLocation;
    private TextView tvExperiencePrice;
    private TextView tvExperienceRating;
    private TextView tvExperienceDuration;
    private TextView tvReviewCount;
    private RecyclerView rvComments;
    private MaterialButton btnBookExperience;
    private View loadingView;
    private View rootView;
    private TextView tvEmptyState;
    private Chip chipCategory;
    private Chip chipFeatured;
    private ImageButton btnBack;
    private ImageButton btnLike;
    private ImageButton btnShare;
    private ImageButton btnSave;
    private TextView tvLikeCount;
    private TextView tvReadMoreDescription;
    private CommentAdapter commentAdapter;
    private CollapsingToolbarLayout collapsingToolbar;

    private String experienceId;
    private Experience currentExperience;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_experience_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;

        if (getArguments() != null) {
            experienceId = getArguments().getString("experience_id");
        }

        initViews(view);
        setupClickListeners();
        loadExperienceDetails();
    }

    private void initViews(View view) {
        ivExperienceImage = view.findViewById(R.id.iv_experience_image);
        ivUserPhoto = view.findViewById(R.id.iv_user_photo);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvPostTime = view.findViewById(R.id.tv_post_time);
        tvExperienceTitle = view.findViewById(R.id.tv_experience_title);
        tvExperienceDescription = view.findViewById(R.id.tv_experience_description);
        tvExperienceLocation = view.findViewById(R.id.tv_experience_location);
        tvExperiencePrice = view.findViewById(R.id.tv_experience_price);
        tvExperienceRating = view.findViewById(R.id.tv_experience_rating);
        tvExperienceDuration = view.findViewById(R.id.tv_experience_duration);
        tvReviewCount = view.findViewById(R.id.tv_review_count);
        rvComments = view.findViewById(R.id.rv_comments);
        btnBookExperience = view.findViewById(R.id.btn_book_experience);
        loadingView = view.findViewById(R.id.loading_view);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
        chipCategory = view.findViewById(R.id.chip_category);
        chipFeatured = view.findViewById(R.id.chip_featured);
        btnBack = view.findViewById(R.id.btn_back);
        btnLike = view.findViewById(R.id.btn_like);
        btnShare = view.findViewById(R.id.btn_share);
        btnSave = view.findViewById(R.id.btn_save);
        tvLikeCount = view.findViewById(R.id.tv_like_count);
        tvReadMoreDescription = view.findViewById(R.id.tv_read_more);
        tvReadMoreDescription = view.findViewById(R.id.tv_read_more);
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);

        // Setup RecyclerView for comments
        rvComments.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvComments.setNestedScrollingEnabled(false);
        commentAdapter = new CommentAdapter(requireContext());
        rvComments.setAdapter(commentAdapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        btnBookExperience.setOnClickListener(v -> {
            // Navigate to booking screen
            showBookingConfirmation();
        });

        btnLike.setOnClickListener(v -> {
            if (currentExperience != null) {
                currentExperience.toggleLike();
                updateLikeButton();
                tvLikeCount.setText(String.format(Locale.getDefault(), "%d likes", currentExperience.getLikeCount()));

                if (currentExperience.isLiked()) {
                    Snackbar.make(rootView, "Added to your liked experiences", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btnShare.setOnClickListener(v -> {
            // Share functionality
            Snackbar.make(rootView, "Sharing this experience", Snackbar.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            // Save/bookmark functionality
            Snackbar.make(rootView, "Experience saved to your profile", Snackbar.LENGTH_SHORT).show();
        });

        tvReadMoreDescription.setOnClickListener(v -> {
            if (tvExperienceDescription.getMaxLines() == 5) {
                // Expand description
                tvExperienceDescription.setMaxLines(Integer.MAX_VALUE);
                tvReadMoreDescription.setText("Read less");
            } else {
                // Collapse description
                tvExperienceDescription.setMaxLines(5);
                tvReadMoreDescription.setText("Read more");
            }
        });
    }

    private void loadExperienceDetails() {
        if (experienceId == null || experienceId.isEmpty()) {
            showEmptyState("Experience not found");
            return;
        }

        showLoading(true);

        // In a real app, this would be an API call
        // For now, just use mock data
        // Simulating API delay
        rootView.postDelayed(() -> {
            // Find the experience by ID from mock data
            List<Experience> mockExperiences = createMockExperiences();
            boolean found = false;

            for (Experience experience : mockExperiences) {
                if (experience.getId().equals(experienceId)) {
                    currentExperience = experience;
                    found = true;
                    break;
                }
            }

            if (!found) {
                // If experience not found, create a default one
                currentExperience = createDefaultExperience();
            }

            displayExperienceDetails(currentExperience);
            showLoading(false);
        }, 1000);
    }

    private void displayExperienceDetails(Experience experience) {
        // Set the title in the collapsing toolbar
        collapsingToolbar.setTitle(experience.getTitle());

        // Set basic experience details
        tvExperienceTitle.setText(experience.getTitle());
        tvExperienceDescription.setText(experience.getDescription());
        tvExperienceLocation.setText(experience.getLocation());
        tvExperiencePrice.setText(String.format(Locale.getDefault(), "$%.0f", experience.getPrice()));
        tvExperienceRating.setText(String.format(Locale.getDefault(), "%.1f", experience.getRating()));
        tvExperienceDuration.setText(experience.getDurationString());

        if (experience.getReviewCount() > 0) {
            tvReviewCount.setText(String.format(Locale.getDefault(), "(%d reviews)", experience.getReviewCount()));
            tvReviewCount.setVisibility(View.VISIBLE);
        } else {
            tvReviewCount.setVisibility(View.GONE);
        }

        // Set category and featured status
        if (experience.getCategory() != null && !experience.getCategory().isEmpty()) {
            chipCategory.setText(experience.getCategory());
            chipCategory.setVisibility(View.VISIBLE);
        } else {
            chipCategory.setVisibility(View.GONE);
        }

        chipFeatured.setVisibility(experience.isFeatured() ? View.VISIBLE : View.GONE);

        // Set user info if available
        if (experience.getUserName() != null && !experience.getUserName().isEmpty()) {
            tvUserName.setText(experience.getUserName());
            if (experience.getCreatedAt() != null) {
                tvPostTime.setText(TimeUtils.getTimeAgo(experience.getCreatedAt()));
            }

            if (experience.getUserPhotoUrl() != null && !experience.getUserPhotoUrl().isEmpty()) {
                Glide.with(this)
                        .load(experience.getUserPhotoUrl())
                        .placeholder(R.drawable.placeholder_profile)
                        .error(R.drawable.placeholder_profile)
                        .into(ivUserPhoto);
            }
        }

        // Set like count and update like button
        tvLikeCount.setText(String.format(Locale.getDefault(), "%d likes", experience.getLikeCount()));
        updateLikeButton();

        // Load image
        if (experience.getImageUrl() != null && !experience.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(experience.getImageUrl())
                    .placeholder(R.drawable.placeholder_experience)
                    .error(R.drawable.placeholder_experience)
                    .centerCrop()
                    .into(ivExperienceImage);
        }

        // Load comments
        if (experience.getComments() != null && !experience.getComments().isEmpty()) {
            commentAdapter.setComments(experience.getComments());
            tvEmptyState.setVisibility(View.GONE);
            rvComments.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setText("No comments yet. Be the first to comment!");
            tvEmptyState.setVisibility(View.VISIBLE);
            rvComments.setVisibility(View.GONE);
        }
    }

    private void updateLikeButton() {
        if (currentExperience != null) {
            if (currentExperience.isLiked()) {
                btnLike.setImageResource(R.drawable.ic_heart_filled);
                btnLike.setColorFilter(requireContext().getResources().getColor(R.color.like_active, null));
            } else {
                btnLike.setImageResource(R.drawable.ic_heart_outliine);
                btnLike.setColorFilter(requireContext().getResources().getColor(R.color.primary_text, null));
            }
        }
    }

    private void showBookingConfirmation() {
        // Show loading indicator
        loadingView.setVisibility(View.VISIBLE);

        // Simulate booking process
        rootView.postDelayed(() -> {
            loadingView.setVisibility(View.GONE);

            // Show success message
            Snackbar.make(rootView, "Booking confirmed for " + currentExperience.getTitle(), Snackbar.LENGTH_LONG)
                    .setAction("View Booking", v -> {
                        // Navigate to bookings
                        Toast.makeText(requireContext(), "Navigating to my bookings", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        }, 2000);
    }

    private void showLoading(boolean show) {
        loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showEmptyState(String message) {
        tvEmptyState.setText(message);
        tvEmptyState.setVisibility(View.VISIBLE);
        showLoading(false);
    }

    // Mock data methods
    private List<Experience> createMockExperiences() {
        List<Experience> experiences = new ArrayList<>();

        // Create the Northern Lights Experience
        Experience northernLights = new Experience();
        northernLights.setId("1");
        northernLights.setTitle("Northern Lights Safari");
        northernLights.setLocation("Tromsø, Norway");
        northernLights.setDescription("Chase the magical Aurora Borealis with expert guides in the Arctic wilderness. Our experienced guides will take you to the best spots away from light pollution to maximize your chances of witnessing this natural phenomenon. The tour includes warm clothing, hot drinks, and photography tips to capture this unforgettable experience.");
        northernLights.setImageUrl("https://picsum.photos/id/110/800/600");
        northernLights.setCategory("Nature");
        northernLights.setRating(4.9f);
        northernLights.setReviewCount(45);
        northernLights.setPrice(129.0);
        northernLights.setDuration(4.0f);
        northernLights.setUserId("user1");
        northernLights.setUserName("Emma Wilson");
        northernLights.setUserPhotoUrl("https://randomuser.me/api/portraits/women/12.jpg");
        northernLights.setLikeCount(245);
        northernLights.setLiked(false);

        // Add comments
        Comment comment1 = new Comment("user2", "James Rodriguez", "https://randomuser.me/api/portraits/men/32.jpg", "I did this tour last winter and it was incredible! We saw the lights dancing across the sky for hours. Definitely worth the money!");
        Comment comment2 = new Comment("user3", "Sophia Chen", "https://randomuser.me/api/portraits/women/44.jpg", "Make sure to bring extra batteries for your camera! Mine died from the cold but the guides had spares.");
        Comment comment3 = new Comment("user4", "Michael Taylor", "https://randomuser.me/api/portraits/men/22.jpg", "Is this tour available in March? That's when I'm planning to visit Norway.");

        northernLights.addComment(comment1);
        northernLights.addComment(comment2);
        northernLights.addComment(comment3);

        experiences.add(northernLights);

        // Create the Cooking Class Experience
        Experience cookingClass = new Experience();
        cookingClass.setId("2");
        cookingClass.setTitle("Traditional Cooking Class");
        cookingClass.setLocation("Marrakech, Morocco");
        cookingClass.setDescription("Learn to prepare authentic Moroccan dishes with local chefs in a traditional riad. This hands-on cooking class will teach you the secrets of Moroccan cuisine, from selecting the perfect spices at the local market to preparing a complete meal including appetizers, main courses, and desserts. You'll enjoy the meal you've prepared in a beautiful courtyard setting.");
        cookingClass.setImageUrl("https://picsum.photos/id/292/800/600");
        cookingClass.setCategory("Food & Drink");
        cookingClass.setRating(4.8f);
        cookingClass.setReviewCount(34);
        cookingClass.setPrice(65.0);
        cookingClass.setDuration(3.0f);
        cookingClass.setUserId("user3");
        cookingClass.setUserName("Sophia Chen");
        cookingClass.setUserPhotoUrl("https://randomuser.me/api/portraits/women/44.jpg");
        cookingClass.setLikeCount(178);
        cookingClass.setLiked(true);

        // Add comments
        Comment comment4 = new Comment("user4", "Michael Taylor", "https://randomuser.me/api/portraits/men/22.jpg", "The chef was amazing and so patient with us! I've been making the tagine recipe at home and it's almost as good as the one we made in class.");
        Comment comment5 = new Comment("user1", "Emma Wilson", "https://randomuser.me/api/portraits/women/12.jpg", "Do they accommodate vegetarians?");

        cookingClass.addComment(comment4);
        cookingClass.addComment(comment5);

        experiences.add(cookingClass);

        // Create the Hot Air Balloon Experience
        Experience balloonRide = new Experience();
        balloonRide.setId("3");
        balloonRide.setTitle("Sunrise Hot Air Balloon Ride");
        balloonRide.setLocation("Cappadocia, Turkey");
        balloonRide.setDescription("Soar above the fairy chimneys and unique landscapes at dawn for breathtaking views. Experience the magic of Cappadocia from the air as the sun rises over this otherworldly landscape. After the flight, enjoy a champagne toast and a full breakfast at a local restaurant. Our experienced pilots ensure a safe and unforgettable journey.");
        balloonRide.setImageUrl("https://picsum.photos/id/283/800/600");
        balloonRide.setCategory("Adventure");
        balloonRide.setRating(4.9f);
        balloonRide.setReviewCount(56);
        balloonRide.setPrice(175.0);
        balloonRide.setDuration(3.5f);
        balloonRide.setLikeCount(312);
        balloonRide.setLiked(false);

        experiences.add(balloonRide);

        return experiences;
    }

    private Experience createDefaultExperience() {
        Experience experience = new Experience();
        experience.setId(experienceId);
        experience.setTitle("Sunrise Hot Air Balloon Ride");
        experience.setLocation("Cappadocia, Turkey");
        experience.setDescription("Soar above the fairy chimneys and unique landscapes at dawn for breathtaking views. Experience the magic of Cappadocia from the air as the sun rises over this otherworldly landscape. After the flight, enjoy a champagne toast and a full breakfast at a local restaurant. Our experienced pilots ensure a safe and unforgettable journey.");
        experience.setImageUrl("https://picsum.photos/id/283/800/600");
        experience.setCategory("Adventure");
        experience.setRating(4.9f);
        experience.setReviewCount(56);
        experience.setPrice(175.0);
        experience.setDuration(3.5f);
        experience.setLikeCount(312);
        experience.setLiked(false);

        return experience;
    }
}