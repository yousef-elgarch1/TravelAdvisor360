package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.ExperienceAdapter;
import com.example.traveladvisor360.interfaces.ExperienceInteractionListener;
import com.example.traveladvisor360.models.Comment;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.utils.ApiCallback;
import com.example.traveladvisor360.utils.ApiClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ExperienceFragment extends Fragment implements ExperienceInteractionListener {

    private RecyclerView rvExperiences;
    private ExperienceAdapter experienceAdapter;
    private ChipGroup categoryChipGroup;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private View loadingView;
    private TextView tvEmptyState;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerLayout;

    private String currentCategory = "All";
    private final Random random = new Random();
    private final String currentUserId = "user123"; // Would be from authentication

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_experiences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        setupCategoryFilter();
        loadExperiences(currentCategory);
    }

    private void initViews(View view) {
        rvExperiences = view.findViewById(R.id.rv_experiences);
        categoryChipGroup = view.findViewById(R.id.category_chip_group);
        tvTitle = view.findViewById(R.id.tv_title);
        tvSubtitle = view.findViewById(R.id.tv_subtitle);
        loadingView = view.findViewById(R.id.loading_view);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        shimmerLayout = view.findViewById(R.id.shimmer_layout);
    }

    private void setupRecyclerView() {
        experienceAdapter = new ExperienceAdapter(requireContext(), this);
        experienceAdapter.setGridView(true); // Use grid view layout

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        // Set the layout manager to handle full-span loading indicator
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == experienceAdapter.getItemCount() - 1 &&
                        experienceAdapter.getItemViewType(position) == 1) {
                    return 2; // Loading item takes full width
                }
                return 1; // Regular items take 1 span
            }
        });

        rvExperiences.setLayoutManager(layoutManager);
        rvExperiences.setAdapter(experienceAdapter);

        // Add scroll listener for pagination
        rvExperiences.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 10) {
                        // Load more experiences (pagination)
                        loadMoreExperiences();
                    }
                }
            }
        });
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.primary,
                R.color.accent,
                R.color.primary_dark
        );

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Refresh data
            loadExperiences(currentCategory);
        });
    }

    private void setupCategoryFilter() {
        String[] categories = {"All", "Nature", "Food & Drink", "Adventure", "Cultural", "Wellness", "City", "Beach", "Wildlife"};

        for (String category : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setChecked(category.equals(currentCategory));
            categoryChipGroup.addView(chip);
        }

        categoryChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != View.NO_ID) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null) {
                    currentCategory = chip.getText().toString();
                    updateTitle();
                    loadExperiences(currentCategory);
                }
            }
        });
    }

    private void updateTitle() {
        if (currentCategory.equals("All")) {
            tvTitle.setText(R.string.unique_experiences);
            tvSubtitle.setText(R.string.immerse_yourself);
        } else {
            tvTitle.setText(currentCategory + " Experiences");
            tvSubtitle.setText("Discover unforgettable " + currentCategory.toLowerCase() + " experiences worldwide");
        }
    }

    private void loadExperiences(String category) {
        showLoading(true);

        // Simulating network delay
        new Handler().postDelayed(() -> {
            // In a real app, this would be an API call
            List<Experience> experiences = createMockExperiences();

            if (!category.equals("All")) {
                // Filter by category
                List<Experience> filteredExperiences = new ArrayList<>();
                for (Experience experience : experiences) {
                    if (experience.getCategory().equals(category)) {
                        filteredExperiences.add(experience);
                    }
                }
                experiences = filteredExperiences;
            }

            if (experiences.isEmpty()) {
                showEmptyState(true);
            } else {
                showEmptyState(false);
            }

            experienceAdapter.setExperiences(experiences);
            showLoading(false);
            swipeRefreshLayout.setRefreshing(false);
        }, 1500);
    }

    private void loadMoreExperiences() {
        experienceAdapter.setLoading(true);

        // Simulate loading more data
        new Handler().postDelayed(() -> {
            List<Experience> currentExperiences = new ArrayList<>(experienceAdapter.getExperiences());
            List<Experience> newExperiences = createMockExperiences();

            if (!currentCategory.equals("All")) {
                // Filter by category
                List<Experience> filteredExperiences = new ArrayList<>();
                for (Experience experience : newExperiences) {
                    if (experience.getCategory().equals(currentCategory)) {
                        filteredExperiences.add(experience);
                    }
                }
                newExperiences = filteredExperiences;
            }

            // Take just 4 new experiences for pagination demo
            if (newExperiences.size() > 4) {
                newExperiences = newExperiences.subList(0, 4);
            }

            currentExperiences.addAll(newExperiences);
            experienceAdapter.setExperiences(currentExperiences);
            experienceAdapter.setLoading(false);
        }, 1500);
    }

    private void showLoading(boolean show) {
        if (show) {
            shimmerLayout.setVisibility(View.VISIBLE);
            shimmerLayout.startShimmer();
            rvExperiences.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.GONE);
        } else {
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.GONE);
            rvExperiences.setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyState(boolean show) {
        tvEmptyState.setVisibility(show ? View.VISIBLE : View.GONE);
        rvExperiences.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private List<Experience> createMockExperiences() {
        List<Experience> experiences = new ArrayList<>();

        // Create users for experiences
        String[][] users = {
                {"user1", "Emma Wilson", "https://randomuser.me/api/portraits/women/12.jpg"},
                {"user2", "James Rodriguez", "https://randomuser.me/api/portraits/men/32.jpg"},
                {"user3", "Sophia Chen", "https://randomuser.me/api/portraits/women/44.jpg"},
                {"user4", "Michael Taylor", "https://randomuser.me/api/portraits/men/22.jpg"}
        };

        // Sample experience data arrays
        String[][] experiencesData = {
                // Title, Location, Description, Category, Rating, Review Count, Price, Duration
                {"Northern Lights Safari", "Tromsø, Norway", "Chase the magical Aurora Borealis with expert guides in the Arctic wilderness.", "Nature", "4.9", "45", "129", "4"},
                {"Traditional Cooking Class", "Marrakech, Morocco", "Learn to prepare authentic Moroccan dishes with local chefs in a traditional riad.", "Food & Drink", "4.8", "34", "65", "3"},
                {"Sunrise Hot Air Balloon Ride", "Cappadocia, Turkey", "Soar above the fairy chimneys and unique landscapes at dawn for breathtaking views.", "Adventure", "4.9", "56", "175", "3.5"},
                {"Ancient Temples Bike Tour", "Siem Reap, Cambodia", "Explore the magnificent temples of Angkor on a guided bicycle tour through the jungle.", "Cultural", "4.7", "28", "45", "6"},
                {"Balinese Spa Retreat", "Ubud, Bali", "Indulge in traditional Indonesian spa treatments in a serene jungle setting.", "Wellness", "4.8", "42", "85", "2"},
                {"Tokyo Street Food Tour", "Tokyo, Japan", "Taste your way through the bustling streets of Tokyo with a local food expert.", "Food & Drink", "4.6", "39", "95", "4"},
                {"Santorini Sunset Cruise", "Santorini, Greece", "Sail around the volcanic caldera and watch the famous sunset from the Aegean Sea.", "Beach", "4.9", "61", "110", "5"},
                {"Wildlife Safari", "Serengeti, Tanzania", "Witness the incredible wildlife of the Serengeti National Park in their natural habitat.", "Wildlife", "4.9", "37", "250", "8"},
                {"City Architecture Tour", "Barcelona, Spain", "Discover Gaudí's masterpieces and the unique architectural heritage of Barcelona.", "City", "4.7", "52", "35", "3.5"},
                {"Glacier Hiking Adventure", "Vatnajökull, Iceland", "Trek across Europe's largest glacier with experienced guides and specialized equipment.", "Adventure", "4.8", "29", "150", "6.5"},
                {"Wine Tasting Experience", "Tuscany, Italy", "Sample exquisite wines in a historic Italian vineyard with panoramic countryside views.", "Food & Drink", "4.7", "48", "75", "3"},
                {"Maldives Snorkeling Trip", "Malé, Maldives", "Explore vibrant coral reefs and encounter colorful marine life in crystal clear waters.", "Beach", "4.9", "55", "95", "4"}
        };

        // Create all experiences
        for (String[] data : experiencesData) {
            Experience experience = new Experience();

            // Basic info
            experience.setId("exp_" + System.currentTimeMillis() + "_" + random.nextInt(1000));
            experience.setTitle(data[0]);
            experience.setLocation(data[1]);
            experience.setDescription(data[2]);
            experience.setCategory(data[3]);
            experience.setRating(Float.parseFloat(data[4]));
            experience.setReviewCount(Integer.parseInt(data[5]));
            experience.setPrice(Double.parseDouble(data[6]));
            experience.setDuration(Float.parseFloat(data[7]));

            // Image URL (in a real app, this would be a real URL)
            experience.setImageUrl("https://picsum.photos/id/" + (random.nextInt(100) + 100) + "/800/600");

            // Social features
            String[] user = users[random.nextInt(users.length)];
            experience.setUserId(user[0]);
            experience.setUserName(user[1]);
            experience.setUserPhotoUrl(user[2]);

            // Create a random date within the last month
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -random.nextInt(30));
            experience.setCreatedAt(cal.getTime());

            experience.setLikeCount(random.nextInt(200));
            experience.setLiked(random.nextBoolean());
            experience.setViewCount(experience.getLikeCount() + random.nextInt(500));
            experience.setFeatured(random.nextInt(8) == 0); // 1 in 8 chance

            // Add comments
            addRandomComments(experience);

            experiences.add(experience);
        }

        return experiences;
    }

    private void addRandomComments(Experience experience) {
        String[] commenters = {"Sarah", "Alex", "Jason", "Maria", "David", "Linda", "Carlos", "Zoe"};
        String[] commentTexts = {
                "This looks amazing! Can't wait to try it.",
                "I did this last year, highly recommend!",
                "The views are even better in person.",
                "Is this suitable for children?",
                "Worth every penny!",
                "How much time did you spend there?",
                "The food was incredible!",
                "Is this available in November?",
                "Do you need to be fit for this?",
                "I'm adding this to my bucket list!"
        };

        // Add 0-3 random comments
        int commentCount = random.nextInt(4);
        for (int i = 0; i < commentCount; i++) {
            String commenterName = commenters[random.nextInt(commenters.length)];
            String commentText = commentTexts[random.nextInt(commentTexts.length)];
            String commenterId = "user_" + commenterName.toLowerCase();
            String commenterPhoto = "https://randomuser.me/api/portraits/" +
                    (random.nextBoolean() ? "women/" : "men/") +
                    random.nextInt(80) + ".jpg";

            Comment comment = new Comment(commenterId, commenterName, commenterPhoto, commentText);

            // Random date within the last week
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, -random.nextInt(168)); // Within last week
            comment.setCreatedAt(cal.getTime());

            comment.setLikeCount(random.nextInt(20));
            experience.addComment(comment);
        }
    }

    // ExperienceInteractionListener Implementation
    @Override
    public void onExperienceClick(Experience experience) {
        // Navigate to experience details
        Bundle args = new Bundle();
        args.putString("experience_id", experience.getId());

        try {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_experiencesFragment_to_experienceDetailsFragment, args);        } catch (Exception e) {
            // For demonstration, show a toast if navigation fails
            Toast.makeText(requireContext(), "Viewing: " + experience.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLikeClick(Experience experience) {
        experience.toggleLike();
        experienceAdapter.notifyDataSetChanged();

        if (experience.isLiked()) {
            Snackbar.make(requireView(), "Added to your liked experiences", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCommentClick(Experience experience) {
        // In real app, show comments dialog or navigate to comments screen
        Snackbar.make(requireView(), "Viewing comments for: " + experience.getTitle(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onShareClick(Experience experience) {
        // Implement share functionality
        Snackbar.make(requireView(), "Sharing: " + experience.getTitle(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBookmarkClick(Experience experience) {
        Snackbar.make(requireView(), "Experience saved!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBookNowClick(Experience experience) {
        // In real app, navigate to booking screen
        Snackbar.make(requireView(), "Booking " + experience.getTitle(), Snackbar.LENGTH_SHORT).show();
    }
}