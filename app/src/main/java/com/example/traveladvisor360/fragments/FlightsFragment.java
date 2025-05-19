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
import com.example.traveladvisor360.adapters.FlightAdapter;
import com.example.traveladvisor360.models.FlightOption;
import com.example.traveladvisor360.models.TripPlanningData;
import com.example.traveladvisor360.network.AmadeusAuthService;
import com.example.traveladvisor360.network.AuthResponse;
import com.example.traveladvisor360.network.FlightApiResponse;
import com.example.traveladvisor360.network.FlightApiService;
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

public class FlightsFragment extends Fragment {

    private RecyclerView flightsRecyclerView;
    private Button nextButton;
    private Button backButton;
    private TextView destinationTextView;

    private TripPlanningData tripData;
    private FlightAdapter adapter;
    private List<FlightOption> availableFlights = new ArrayList<>();
    private FlightOption selectedFlight;

    private String bearerToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripData = TripPlanningData.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flights, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        flightsRecyclerView = view.findViewById(R.id.recycler_flights);
        nextButton = view.findViewById(R.id.btn_next);
        backButton = view.findViewById(R.id.btn_back);
        destinationTextView = view.findViewById(R.id.text_destination);

        // Update destination text
        destinationTextView.setText(String.format("Flights to %s", tripData.getDestination()));

        // Set up RecyclerView
        setupFlightsRecyclerView();

        // Fetch bearer token and then load flights
        fetchBearerTokenAndLoadFlights();

        // Set up click listeners
        nextButton.setOnClickListener(v -> {
            if (validateFlightSelection()) {
                saveSelectedFlight();
                navigateToHotelSelection();
            }
        });

        backButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void fetchBearerTokenAndLoadFlights() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.api.amadeus.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AmadeusAuthService authService = retrofit.create(AmadeusAuthService.class);

        Call<AuthResponse> call = authService.getBearerToken(
                "client_credentials",
                "AUtDvsVxTBZewrEhGuVpLLMNa0c0QKuI",    // Replace with your Amadeus API key
                "RQrl5y5OvGlBs5Dv"  // Replace with your Amadeus API secret
        );

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    bearerToken = "Bearer " + response.body().access_token;
                    tripData.setBearerToken(bearerToken);
                    loadFlightsForDestination();
                } else {
                    Snackbar.make(requireView(), "Failed to authenticate with Amadeus", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Snackbar.make(requireView(), "Error fetching token: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFlightsForDestination() {
        if (bearerToken == null) {
            Snackbar.make(requireView(), "No bearer token available", Snackbar.LENGTH_SHORT).show();
            return;
        }

        String origin = tripData.getDeparture();
        String destination = tripData.getDestination(); // Should be IATA code, e.g., "CDG"
        String departureDate = tripData.getStartDate() != null ? tripData.getStartDate() : "2024-07-01";
        int adults = 1;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.api.amadeus.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FlightApiService apiService = retrofit.create(FlightApiService.class);

        apiService.searchFlights(bearerToken, origin, destination, departureDate, adults)
                .enqueue(new Callback<FlightApiResponse>() {
                    @Override
                    public void onResponse(Call<FlightApiResponse> call, Response<FlightApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            availableFlights.clear();
                            for (FlightApiResponse.FlightOffer offer : response.body().data) {
                                // Get the last segment of the first itinerary
                                FlightApiResponse.Segment lastSegment = offer.itineraries.get(0).segments
                                        .get(offer.itineraries.get(0).segments.size() - 1);
                                // Only add if arrival airport matches the selected destination
                                FlightApiResponse.Segment firstSegment = offer.itineraries.get(0).segments.get(0);
                                if (
                                        firstSegment.departure.iataCode.equalsIgnoreCase(origin) &&
                                                lastSegment.arrival.iataCode.equalsIgnoreCase(destination)
                                ) {
                                    availableFlights.add(new FlightOption(
                                            offer.travelerPricings.get(0).fareDetailsBySegment.get(0).cabin,
                                            lastSegment.carrierCode + lastSegment.number,
                                            lastSegment.carrierCode,
                                            firstSegment.departure.iataCode,
                                            lastSegment.arrival.iataCode,
                                            firstSegment.departure.at,
                                            lastSegment.arrival.at,
                                            offer.price.total,
                                            offer.itineraries.get(0).segments.size() - 1,
                                            false
                                    ));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Snackbar.make(requireView(), "No flights found", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FlightApiResponse> call, Throwable t) {
                        Snackbar.make(requireView(), "Failed to fetch flights", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupFlightsRecyclerView() {
        adapter = new FlightAdapter(availableFlights);
        flightsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        flightsRecyclerView.setAdapter(adapter);

        // Set selection listener
        adapter.setOnFlightSelectedListener(flight -> {
            if (selectedFlight != null) {
                selectedFlight.setSelected(false);
            }
            selectedFlight = flight;
            selectedFlight.setSelected(true);
            adapter.notifyDataSetChanged();
        });
    }

    private boolean validateFlightSelection() {
        if (selectedFlight == null) {
            Snackbar.make(requireView(), "Please select a flight", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveSelectedFlight() {
        tripData.setSelectedFlight(selectedFlight.getFlightNumber());

        Map<String, Object> flightDetails = new HashMap<>();
        flightDetails.put("airline", selectedFlight.getAirline());
        flightDetails.put("class", selectedFlight.getTravelClass());
        flightDetails.put("departure", selectedFlight.getDepartureTime());
        flightDetails.put("arrival", selectedFlight.getArrivalTime());
        flightDetails.put("price", selectedFlight.getPrice());
        flightDetails.put("stops", selectedFlight.getStops());

        tripData.setFlightDetails(flightDetails);
    }

    private void navigateToHotelSelection() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_flightsFragment_to_hotelsFragment);
    }
}