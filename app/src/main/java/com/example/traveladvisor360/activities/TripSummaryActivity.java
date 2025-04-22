package com.example.traveladvisor360.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


import com.example.traveladvisor360.R;

public class TripSummaryActivity extends AppCompatActivity {

    private TextView destinationTextView, budgetTextView, tripTypeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summary);

        destinationTextView = findViewById(R.id.destinationTextView);
        budgetTextView = findViewById(R.id.budgetTextView);
        tripTypeTextView = findViewById(R.id.tripTypeTextView);

        // Retrieve data from the intent
        String destination = getIntent().getStringExtra("DESTINATION");
        String budget = getIntent().getStringExtra("BUDGET");
        String tripType = getIntent().getStringExtra("TRIP_TYPE");

        destinationTextView.setText(destination);
        budgetTextView.setText(budget);
        tripTypeTextView.setText(tripType);
    }
}
