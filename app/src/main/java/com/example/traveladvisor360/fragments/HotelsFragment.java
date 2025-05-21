package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.HotelAdapter;
import com.example.traveladvisor360.models.HotelOption;
import com.example.traveladvisor360.models.TripPlanningData;
import com.example.traveladvisor360.network.HotelApiResponse;
import com.example.traveladvisor360.network.HotelApiService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HotelsFragment extends Fragment {

    private RecyclerView hotelsRecyclerView;
    private Button nextButton;
    private Button backButton;
    private TextView destinationTextView;

    private TripPlanningData tripData;
    private HotelAdapter adapter;
    private List<HotelOption> availableHotels = new ArrayList<>();
    private HotelOption selectedHotel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripData = TripPlanningData.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hotels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hotelsRecyclerView = view.findViewById(R.id.recycler_hotels);
        nextButton = view.findViewById(R.id.btn_next);
        backButton = view.findViewById(R.id.btn_back);
        destinationTextView = view.findViewById(R.id.text_destination);

        // Show city name if available, else fallback to IATA code
        String cityName = tripData.getDestinationCityName() != null
                ? tripData.getDestinationCityName()
                : tripData.getDestination();
        destinationTextView.setText(String.format("Hotels in %s", cityName));

        setupHotelsRecyclerView();
        loadHotelsForDestination();

        nextButton.setOnClickListener(v -> {
            if (validateHotelSelection()) {
                saveSelectedHotel();
                navigateToActivities();
            }
        });

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void loadHotelsForDestination() {
        String bearerToken = tripData.getBearerToken();
        String cityCode = tripData.getDestination();
        Integer radius = 20;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.api.amadeus.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HotelApiService hotelApi = retrofit.create(HotelApiService.class);

        hotelApi.searchHotels(bearerToken, cityCode, radius)
                .enqueue(new Callback<HotelApiResponse>() {
                    @Override
                    public void onResponse(Call<HotelApiResponse> call, Response<HotelApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            availableHotels.clear();
                            for (HotelApiResponse.HotelData hotel : response.body().data) {
                                availableHotels.add(new HotelOption(
                                        hotel.name,
                                        hotel.address != null ? hotel.address.cityName : "",
                                        hotel.description != null ? hotel.description.text : "",
                                        hotel.rating,
                                        hotel.price != null ? hotel.price.total : 0,
                                        hotel.rating > 0 ? (int) hotel.rating : 0,
                                        false
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Snackbar.make(requireView(), "No hotels found", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<HotelApiResponse> call, Throwable t) {
                        Snackbar.make(requireView(), "Failed to fetch hotels", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupHotelsRecyclerView() {
        adapter = new HotelAdapter(availableHotels);
        hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        hotelsRecyclerView.setAdapter(adapter);

        adapter.setOnHotelSelectedListener(hotel -> {
            if (selectedHotel != null) {
                selectedHotel.setSelected(false);
            }
            selectedHotel = hotel;
            selectedHotel.setSelected(true);
            adapter.notifyDataSetChanged();
        });
    }

    private boolean validateHotelSelection() {
        if (selectedHotel == null) {
            Snackbar.make(requireView(), "Please select a hotel", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveSelectedHotel() {
        tripData.setSelectedHotel(selectedHotel.getName());
        Map<String, Object> hotelDetails = new HashMap<>();
        hotelDetails.put("location", selectedHotel.getLocation());
        hotelDetails.put("description", selectedHotel.getDescription());
        hotelDetails.put("rating", selectedHotel.getRating());
        hotelDetails.put("price", selectedHotel.getPricePerNight());
        hotelDetails.put("stars", selectedHotel.getStars());
        tripData.setHotelDetails(hotelDetails);
    }

    private void navigateToActivities() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_hotelsFragment_to_activitiesFragment);
    }
}