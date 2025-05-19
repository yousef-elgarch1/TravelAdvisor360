package com.example.traveladvisor360.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
import com.example.traveladvisor360.adapters.ActivitySummaryAdapter;
import com.example.traveladvisor360.adapters.CompanionSummaryAdapter;
import com.example.traveladvisor360.models.TravelCompanion;
import com.example.traveladvisor360.models.TripPlanningData;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;

public class TripSummaryFragment extends Fragment {

    private TextView titleTextView;
    private TextView destinationTextView;
    private TextView budgetTextView;
    private TextView flightTextView;
    private TextView hotelTextView;
    private RecyclerView activitiesRecyclerView;
    private RecyclerView companionsRecyclerView;
    private MaterialCardView companionsCardView;
    private Button shareButton;
    private Button editButton;
    private Button finishButton;

    private TripPlanningData tripData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripData = TripPlanningData.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        titleTextView = view.findViewById(R.id.text_summary_title);
        destinationTextView = view.findViewById(R.id.text_destination);
        budgetTextView = view.findViewById(R.id.text_budget);
        flightTextView = view.findViewById(R.id.text_flight_details);
        hotelTextView = view.findViewById(R.id.text_hotel_details);
        activitiesRecyclerView = view.findViewById(R.id.recycler_activities);
        companionsRecyclerView = view.findViewById(R.id.recycler_companions);
        companionsCardView = view.findViewById(R.id.card_companions);
        shareButton = view.findViewById(R.id.btn_share);
        editButton = view.findViewById(R.id.btn_edit);
        finishButton = view.findViewById(R.id.btn_finish);

        // Fill summary data
        populateSummaryData();

        // Set up click listeners
        shareButton.setOnClickListener(v -> shareTrip());

        editButton.setOnClickListener(v -> {
            // Navigate back to the beginning of the planning process
            Navigation.findNavController(view)
                    .navigate(R.id.action_tripSummaryFragment_to_destinationBudgetFragment);
        });

        finishButton.setOnClickListener(v -> {
            // Complete planning and go back to home
            Navigation.findNavController(view)
                    .navigate(R.id.action_tripSummaryFragment_to_homeFragment);
            // Reset planning data for next trip
            tripData.reset();
        });
    }


    private void populateSummaryData() {
        // Set title based on trip type
        String tripType = tripData.getTripType();
        if ("solo".equals(tripType)) {
            titleTextView.setText("Your Solo Trip Summary");
            companionsCardView.setVisibility(View.GONE);
        } else {
            titleTextView.setText("Your Group Trip Summary");
            companionsCardView.setVisibility(View.VISIBLE);
        }

        // Set destination and budget
        destinationTextView.setText(tripData.getDestination());

        // Format currency
        String currencyCode = tripData.getCurrency();
        if (currencyCode != null && currencyCode.length() >= 3) {
            currencyCode = currencyCode.substring(0, 3); // Extract USD from "USD ($)"
        } else {
            currencyCode = "USD";
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        try {
            currencyFormat.setCurrency(Currency.getInstance(currencyCode));
        } catch (IllegalArgumentException e) {
            currencyFormat.setCurrency(Currency.getInstance("USD"));
        }

        budgetTextView.setText(currencyFormat.format(tripData.getBudget()));

        // Set flight details
        Map<String, Object> flightDetails = tripData.getFlightDetails();
        if (flightDetails != null && !flightDetails.isEmpty()) {
            String flightInfo = String.format("%s - %s\n%s, %s\nDeparture: %s, Arrival: %s\nPrice: %s",
                    tripData.getSelectedFlight(),
                    flightDetails.get("airline"),
                    flightDetails.get("class"),
                    (int)flightDetails.get("stops") == 0 ? "Non-stop" : (int)flightDetails.get("stops") + " stop(s)",
                    flightDetails.get("departure"),
                    flightDetails.get("arrival"),
                    currencyFormat.format((double)flightDetails.get("price")));

            flightTextView.setText(flightInfo);
        }

        // Set hotel details
        Map<String, Object> hotelDetails = tripData.getHotelDetails();
        if (hotelDetails != null && !hotelDetails.isEmpty()) {
            String hotelInfo = String.format("%s\n%s\n%s\nRating: %.1f/5.0, %d★\nPrice per night: %s",
                    tripData.getSelectedHotel(),
                    hotelDetails.get("location"),
                    hotelDetails.get("description"),
                    (double)hotelDetails.get("rating"),
                    (int)hotelDetails.get("stars"),
                    currencyFormat.format((double)hotelDetails.get("price")));

            hotelTextView.setText(hotelInfo);
        }

        // Set up activities
        ActivitySummaryAdapter activityAdapter = new ActivitySummaryAdapter(tripData.getSelectedActivities());
        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        activitiesRecyclerView.setAdapter(activityAdapter);

        // Set up companions if group trip
        if ("group".equals(tripType) && !tripData.getCompanions().isEmpty()) {
            CompanionSummaryAdapter companionAdapter = new CompanionSummaryAdapter(tripData.getCompanions());
            companionsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            companionsRecyclerView.setAdapter(companionAdapter);
        }
    }

    private void shareTrip() {
        // Create share content
        StringBuilder shareContent = new StringBuilder();

        shareContent.append("My Trip to ").append(tripData.getDestination()).append("\n\n");

        // Add trip details
        shareContent.append("Flight: ").append(tripData.getSelectedFlight()).append("\n");
        shareContent.append("Hotel: ").append(tripData.getSelectedHotel()).append("\n\n");

        // Add activities
        shareContent.append("Activities:\n");
        for (String activity : tripData.getSelectedActivities()) {
            shareContent.append("- ").append(activity).append("\n");
        }

        // Add companions for group trips
        if ("group".equals(tripData.getTripType()) && !tripData.getCompanions().isEmpty()) {
            shareContent.append("\nTravel companions:\n");
            for (TravelCompanion companion : tripData.getCompanions()) {
                shareContent.append("- ").append(companion.getName()).append("\n");
            }
        }

        // Create and launch share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Trip to " + tripData.getDestination());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent.toString());

        startActivity(Intent.createChooser(shareIntent, "Share trip details via"));
    }
}