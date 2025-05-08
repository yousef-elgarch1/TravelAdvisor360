package com.example.traveladvisor360.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.AttractionAdapter;
import com.example.traveladvisor360.adapters.ExperienceAdapter;
import com.example.traveladvisor360.adapters.HotelAdapter;
import com.example.traveladvisor360.models.Destination;
import com.example.traveladvisor360.models.Experience;
import com.example.traveladvisor360.models.Hotel;
import com.example.traveladvisor360.models.HotelOption;
import com.example.traveladvisor360.utils.ApiCallback;
import com.example.traveladvisor360.utils.ApiClient;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

public class DestinationDetailsFragment extends Fragment {

    private static final String ARG_DESTINATION_ID = "destination_id";

    private ImageView ivCoverImage;
    private CollapsingToolbarLayout collapsingToolbar;
    private TextView tvDescription;
    private TextView tvWeatherInfo;
    private TextView tvBestTimeToVisit;
    private RecyclerView rvAttractions;
    private RecyclerView rvNearbyHotels;
    private RecyclerView rvNearbyExperiences;
    private Button btnStartPlanning;
    private AttractionAdapter attractionAdapter;
    private HotelAdapter hotelAdapter;
    private ExperienceAdapter experienceAdapter;
    private String destinationId;


    private Destination destination;

    public static DestinationDetailsFragment newInstance(String destinationId) {
        DestinationDetailsFragment fragment = new DestinationDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DESTINATION_ID, destinationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            destinationId = getArguments().getString(ARG_DESTINATION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_destination_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        loadDestinationDetails();

        btnStartPlanning.setOnClickListener(v -> {
            navigateToPlanning();
        });
    }

    private void initViews(View view) {
        ivCoverImage = view.findViewById(R.id.iv_cover_image);
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        tvDescription = view.findViewById(R.id.tv_description);
        tvWeatherInfo = view.findViewById(R.id.tv_weather_info);
        tvBestTimeToVisit = view.findViewById(R.id.tv_best_time_to_visit);
        rvAttractions = view.findViewById(R.id.rv_attractions);
        rvNearbyHotels = view.findViewById(R.id.rv_nearby_hotels);
        rvNearbyExperiences = view.findViewById(R.id.rv_nearby_experiences);
        btnStartPlanning = view.findViewById(R.id.btn_start_planning);

        setupRecyclerViews();
    }

    private void setupRecyclerViews() {
        attractionAdapter = new AttractionAdapter(requireContext());
        rvAttractions.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvAttractions.setAdapter(attractionAdapter);

        hotelAdapter = new HotelAdapter((List<HotelOption>) requireContext());
        rvNearbyHotels.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNearbyHotels.setAdapter(hotelAdapter);

        experienceAdapter = new ExperienceAdapter(requireContext());
        rvNearbyExperiences.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNearbyExperiences.setAdapter(experienceAdapter);
    }

    private void loadDestinationDetails() {
        ApiClient.getInstance().getDestination(destinationId, new ApiCallback<Destination>() {
            @Override
            public void onSuccess(Destination result) {
                requireActivity().runOnUiThread(() -> {
                    destination = result;
                    updateUI();
                });
            }

            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void updateUI() {
        collapsingToolbar.setTitle(destination.getName());

        Glide.with(this)
                .load(getImageResource(destination.getImageUrl()))
                .centerCrop()
                .into(ivCoverImage);

        tvDescription.setText(destination.getDescription());
        tvWeatherInfo.setText(destination.getWeatherInfo());
        tvBestTimeToVisit.setText(destination.getBestTimeToVisit());

        loadAttractions();
        loadNearbyHotels();
        loadNearbyExperiences();
    }

    private int getImageResource(String imageName) {
        return requireContext().getResources().getIdentifier(
                imageName, "drawable", requireContext().getPackageName());
    }

    private void loadAttractions() {
        List<String> attractions = destination.getAttractions();
        attractionAdapter.setAttractions(attractions);
    }

    private void loadNearbyHotels() {
        ApiClient.getInstance().getNearbyHotels(destination.getId(), new ApiCallback<List<Hotel>>() {
            @Override
            public void onSuccess(List<Hotel> result) {
                requireActivity().runOnUiThread(() -> {
                    hotelAdapter.setHotels(result);
                });
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

    private void loadNearbyExperiences() {
        ApiClient.getInstance().getNearbyExperiences(destination.getId(), new ApiCallback<List<Experience>>() {
            @Override
            public void onSuccess(List<Experience> result) {
                requireActivity().runOnUiThread(() -> {
                    experienceAdapter.setExperiences(result);
                });
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }

    private void navigateToPlanning() {
        Bundle args = new Bundle();
        args.putString("destination_id", destination.getId());
        args.putString("destination_name", destination.getName());

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_destinationDetailsFragment_to_tripPlanningActivity, args);
    }
}


