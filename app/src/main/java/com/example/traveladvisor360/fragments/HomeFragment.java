package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.DestinationAdapter;
import com.example.traveladvisor360.adapters.ExperienceAdapter;
import com.example.traveladvisor360.models.Destination;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.utils.ApiCallback;
import com.example.traveladvisor360.utils.ApiClient;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvPopularDestinations;
    private RecyclerView rvFeaturedExperiences;
    private DestinationAdapter destinationAdapter;
    private ExperienceAdapter experienceAdapter;
    private Button btnStartPlanning;
    private Button btnExploreDestinations;
    private Button btnSearch;
    private TextInputEditText etSearchInput;
    private TabLayout searchTabs;
    private MaterialCardView searchCard;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        // Initialize views
        initViews(view);

        // Setup click listeners
        setupClickListeners();

        // Load data
        loadDestinations();
        loadExperiences();
    }

    private void initViews(View view) {
        rvPopularDestinations = view.findViewById(R.id.rv_popular_destinations);
        rvFeaturedExperiences = view.findViewById(R.id.rv_featured_experiences);
        btnStartPlanning = view.findViewById(R.id.btn_start_planning);
        btnExploreDestinations = view.findViewById(R.id.btn_explore_destinations);
        btnSearch = view.findViewById(R.id.btn_search);
        etSearchInput = view.findViewById(R.id.search_input);
        searchTabs = view.findViewById(R.id.search_tabs);
        searchCard = view.findViewById(R.id.search_card);

        // Setup RecyclerViews
        setupRecyclerViews();
    }

    private void setupRecyclerViews() {
        // Destinations RecyclerView
        LinearLayoutManager destinationsLayoutManager = new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPopularDestinations.setLayoutManager(destinationsLayoutManager);
        destinationAdapter = new DestinationAdapter(requireContext());
        rvPopularDestinations.setAdapter(destinationAdapter);

        // Set click listener for destinations
        destinationAdapter.setOnDestinationClickListener(destination -> {
            Bundle args = new Bundle();
            args.putString("destination_id", destination.getId());
            navController.navigate(R.id.destinationsFragment, args);
        });

        // Experiences RecyclerView
        LinearLayoutManager experiencesLayoutManager = new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFeaturedExperiences.setLayoutManager(experiencesLayoutManager);
        experienceAdapter = new ExperienceAdapter(requireContext());
        rvFeaturedExperiences.setAdapter(experienceAdapter);

        // Set click listener for experiences
        experienceAdapter.setOnExperienceClickListener(experience -> {
            Bundle args = new Bundle();
            args.putString("experience_id", experience.getId());
            navController.navigate(R.id.action_experiencesFragment_to_experienceDetailsFragment, args);
        });
    }

    private void setupClickListeners() {
        btnStartPlanning.setOnClickListener(v -> showTripPlanningDialog());

        btnExploreDestinations.setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_destinationsFragment);
        });

        btnSearch.setOnClickListener(v -> {
            String query = etSearchInput.getText().toString().trim();
            if (!query.isEmpty()) {
                performSearch(query);
            } else {
                Toast.makeText(requireContext(), "Please enter a destination", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup tab selection
        searchTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection
                updateSearchUI(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselection
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselection
            }
        });
    }

    private void showTripPlanningDialog() {
        PlanningOptionsDialog dialog = new PlanningOptionsDialog();
        dialog.show(getChildFragmentManager(), "PlanningOptions");
    }

    private void performSearch(String query) {
        // Navigate to search results or perform search
        Bundle args = new Bundle();
        args.putString("search_query", query);
        navController.navigate(R.id.action_homeFragment_to_searchResultsFragment, args);
    }

    private void updateSearchUI(int tabPosition) {
        // Update UI based on selected tab
        switch (tabPosition) {
            case 0: // Destination
                etSearchInput.setHint("Search destinations");
                break;
            case 1: // Dates
                etSearchInput.setHint("Select dates");
                // Could show date picker here
                break;
            case 2: // Travelers
                etSearchInput.setHint("Number of travelers");
                // Could show number picker here
                break;
        }
    }

    private void loadDestinations() {
        // Get popular destinations from API or local data
        ApiClient.getInstance(requireContext()).getDestinations(new ApiCallback<List<Destination>>() {
            @Override
            public void onSuccess(List<Destination> result) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        destinationAdapter.setDestinations(result);
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        // For demo, use mock data
                        List<Destination> mockDestinations = getMockDestinations();
                        destinationAdapter.setDestinations(mockDestinations);
                    });
                }
            }
        });
    }

    private void loadExperiences() {
        // Get featured experiences from API or local data
        ApiClient.getInstance().getExperiences(new ApiCallback<List<Experience>>() {
            @Override
            public void onSuccess(List<Experience> result) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        experienceAdapter.setExperiences(result);
                    });
                }
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        // For demo, use mock data
                        List<Experience> mockExperiences = getMockExperiences();
                        experienceAdapter.setExperiences(mockExperiences);
                    });
                }
            }
        });
    }

    private List<Destination> getMockDestinations() {
        List<Destination> destinations = new ArrayList<>();

        // Paris
        Destination paris = new Destination();
        paris.setId("1");
        paris.setName("Paris");
        paris.setCountry("France");
        paris.setCity("Paris");
        paris.setDescription("The City of Light offers iconic landmarks, world-class cuisine, and romantic ambiance.");
        paris.setImageUrl("paris");
        paris.setRating(4.8f);
        paris.setReviewCount(1253);
        paris.setTags(Arrays.asList("Romantic", "Cultural", "Historic"));
        destinations.add(paris);

        // Tokyo
        Destination tokyo = new Destination();
        tokyo.setId("2");
        tokyo.setName("Tokyo");
        tokyo.setCountry("Japan");
        tokyo.setCity("Tokyo");
        tokyo.setDescription("A fascinating blend of traditional culture and ultra-modern technology.");
        tokyo.setImageUrl("tokyo");
        tokyo.setRating(4.7f);
        tokyo.setReviewCount(982);
        tokyo.setTags(Arrays.asList("Modern", "Cultural", "Food"));
        destinations.add(tokyo);

        // Seoul
        Destination seoul = new Destination();
        seoul.setId("3");
        seoul.setName("Seoul");
        seoul.setCountry("South Korea");
        seoul.setCity("Seoul");
        seoul.setDescription("A vibrant metropolis where ancient palaces meet K-pop culture and cutting-edge technology.");
        seoul.setImageUrl("seoul");
        seoul.setRating(4.6f);
        seoul.setReviewCount(873);
        seoul.setTags(Arrays.asList("Modern", "Cultural", "Shopping"));
        destinations.add(seoul);

// Mecca
        Destination mecca = new Destination();
        mecca.setId("4");
        mecca.setName("Mecca");
        mecca.setCountry("Saudi Arabia");
        mecca.setCity("Mecca");
        mecca.setDescription("The holiest city in Islam and the birthplace of Prophet Muhammad, drawing millions of pilgrims annually.");
        mecca.setImageUrl("mecca");
        mecca.setRating(4.9f);
        mecca.setReviewCount(1542);
        mecca.setTags(Arrays.asList("Religious", "Historic", "Cultural"));
        destinations.add(mecca);

// Dubai
        Destination dubai = new Destination();
        dubai.setId("5");
        dubai.setName("Dubai");
        dubai.setCountry("United Arab Emirates");
        dubai.setCity("Dubai");
        dubai.setDescription("A futuristic city of record-breaking skyscrapers, luxury shopping, and desert adventures.");
        dubai.setImageUrl("dubai");
        dubai.setRating(4.8f);
        dubai.setReviewCount(1325);
        dubai.setTags(Arrays.asList("Luxury", "Modern", "Shopping"));
        destinations.add(dubai);

// Beijing (China)
        Destination beijing = new Destination();
        beijing.setId("6");
        beijing.setName("Beijing");
        beijing.setCountry("China");
        beijing.setCity("Beijing");
        beijing.setDescription("China's historic capital featuring the Forbidden City, Great Wall, and a blend of imperial heritage and modernity.");
        beijing.setImageUrl("beijing");
        beijing.setRating(4.7f);
        beijing.setReviewCount(1105);
        beijing.setTags(Arrays.asList("Historic", "Cultural", "Imperial"));
        destinations.add(beijing);

// Rabat
        Destination rabat = new Destination();
        rabat.setId("7");
        rabat.setName("Rabat");
        rabat.setCountry("Morocco");
        rabat.setCity("Rabat");
        rabat.setDescription("Morocco's capital city with beautiful beaches, historic Kasbah, and elegant Andalusian gardens.");
        rabat.setImageUrl("rabat");
        rabat.setRating(4.5f);
        rabat.setReviewCount(652);
        rabat.setTags(Arrays.asList("Historic", "Coastal", "Cultural"));
        destinations.add(rabat);

        return destinations;
    }

    private List<Experience> getMockExperiences() {
        List<Experience> experiences = new ArrayList<>();

        // Northern Lights Safari
        Experience northernLights = new Experience();
        northernLights.setId("1");
        northernLights.setTitle("Northern Lights Safari");
        northernLights.setLocation("Tromsø, Norway");
        northernLights.setDescription("Chase the magical Aurora Borealis with expert guides in the Arctic wilderness.");
        northernLights.setImageUrl("lights");
        northernLights.setCategory("Nature");
        northernLights.setRating(4.9f);
        northernLights.setReviewCount(45);
        northernLights.setPrice(129.0);
        northernLights.setDuration(4.0f);
        experiences.add(northernLights);

        // Traditional Cooking Class
        Experience cookingClass = new Experience();
        cookingClass.setId("2");
        cookingClass.setTitle("Traditional Cooking Class");
        cookingClass.setLocation("Marrakech, Morocco");
        cookingClass.setDescription("Learn to prepare authentic Moroccan dishes with local chefs in a traditional riad.");
        cookingClass.setImageUrl("cooking");
        cookingClass.setCategory("Food & Drink");
        cookingClass.setRating(4.8f);
        cookingClass.setReviewCount(34);
        cookingClass.setPrice(65.0);
        cookingClass.setDuration(3.0f);
        experiences.add(cookingClass);

        // Sunrise Hot Air Balloon Ride
        Experience balloonRide = new Experience();
        balloonRide.setId("3");
        balloonRide.setTitle("Sunrise Hot Air Balloon Ride");
        balloonRide.setLocation("Cappadocia, Turkey");
        balloonRide.setDescription("Soar above the fairy chimneys and unique landscapes at dawn for breathtaking views.");
        balloonRide.setImageUrl("ballon");
        balloonRide.setCategory("Adventure");
        balloonRide.setRating(4.9f);
        balloonRide.setReviewCount(56);
        balloonRide.setPrice(175.0);
        balloonRide.setDuration(3.5f);
        experiences.add(balloonRide);

        // Ancient Temples Bike Tour
        Experience templeTour = new Experience();
        templeTour.setId("4");
        templeTour.setTitle("Ancient Temples Bike Tour");
        templeTour.setLocation("Siem Reap, Cambodia");
        templeTour.setDescription("Explore the magnificent temples of Angkor on a guided bicycle tour through the jungle.");
        templeTour.setImageUrl("temples");
        templeTour.setCategory("Cultural");
        templeTour.setRating(4.7f);
        templeTour.setReviewCount(28);
        templeTour.setPrice(45.0);
        templeTour.setDuration(6.0f);
        experiences.add(templeTour);

        return experiences;
    }
}