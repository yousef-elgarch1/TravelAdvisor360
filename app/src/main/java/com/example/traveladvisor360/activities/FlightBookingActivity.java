package com.example.traveladvisor360.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.FlightAdapter;
import com.example.traveladvisor360.models.Flight;
import com.example.traveladvisor360.models.FlightOption;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightBookingActivity extends AppCompatActivity {

    private TabLayout tripTypeTabs;
    private TextInputEditText etFrom;
    private TextInputEditText etTo;
    private TextInputEditText etDepart;
    private TextInputEditText etReturn;
    private Spinner spinnerPassengers;
    private Spinner spinnerClass;
    private RecyclerView rvFlights;
    private FlightAdapter flightAdapter;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_booking);

        initViews();
        setupTabs();
        setupRecyclerView();
        setupDatePickers();
        setupSearchButton();
    }

    private void initViews() {
        tripTypeTabs = findViewById(R.id.trip_type_tabs);
        etFrom = findViewById(R.id.et_from);
        etTo = findViewById(R.id.et_to);
        etDepart = findViewById(R.id.et_depart);
        etReturn = findViewById(R.id.et_return);
        spinnerPassengers = findViewById(R.id.spinner_passengers);
        spinnerClass = findViewById(R.id.spinner_class);
        rvFlights = findViewById(R.id.rv_flights);
        btnSearch = findViewById(R.id.btn_search);
    }

    private void setupTabs() {
        tripTypeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) { // Round Trip
                    etReturn.setEnabled(true);
                } else { // One Way
                    etReturn.setEnabled(false);
                    etReturn.setText("");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerView() {
        rvFlights.setLayoutManager(new LinearLayoutManager(this));
        flightAdapter = new FlightAdapter((List<FlightOption>) this);
        rvFlights.setAdapter(flightAdapter);
    }

    private void setupDatePickers() {
        etDepart.setOnClickListener(v -> showDatePicker(etDepart));
        etReturn.setOnClickListener(v -> showDatePicker(etReturn));
    }

    private void showDatePicker(TextInputEditText editText) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            editText.setText(new java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault())
                    .format(new java.util.Date(selection)));
        });

        datePicker.show(getSupportFragmentManager(), "DatePicker");
    }

    private void setupSearchButton() {
        btnSearch.setOnClickListener(v -> searchFlights());
    }

    private void searchFlights() {
        // Get search parameters
        String from = etFrom.getText().toString();
        String to = etTo.getText().toString();
        String departDate = etDepart.getText().toString();
        String returnDate = etReturn.getText().toString();
        int passengers = spinnerPassengers.getSelectedItemPosition() + 1;
        String flightClass = (String) spinnerClass.getSelectedItem();

        // Mock flight data
        List<Flight> flights = createMockFlights();
        flightAdapter.setFlights(flights);
    }

    private List<Flight> createMockFlights() {
        List<Flight> flights = new ArrayList<>();

        Flight flight1 = new Flight();
        flight1.setId("1");
        flight1.setAirline("Air France");
        flight1.setAirlineCode("AF");
        flight1.setFlightNumber("AF123");
        flight1.setDepartureAirport("JFK");
        flight1.setArrivalAirport("CDG");
        flight1.setDepartureCity("New York");
        flight1.setArrivalCity("Paris");
        flight1.setDepartureTime(new Date());
        flight1.setArrivalTime(new Date(System.currentTimeMillis() + 8 * 60 * 60 * 1000));
        flight1.setDuration(480); // 8 hours
        flight1.setPrice(320);
        flight1.setDirect(true);
        flight1.setCabinClass("Economy");
        flights.add(flight1);

        Flight flight2 = new Flight();
        flight2.setId("2");
        flight2.setAirline("British Airways");
        flight2.setAirlineCode("BA");
        flight2.setFlightNumber("BA456");
        flight2.setDepartureAirport("JFK");
        flight2.setArrivalAirport("CDG");
        flight2.setDepartureCity("New York");
        flight2.setArrivalCity("Paris");
        flight2.setDepartureTime(new Date());
        flight2.setArrivalTime(new Date(System.currentTimeMillis() + 9 * 60 * 60 * 1000));
        flight2.setDuration(540); // 9 hours
        flight2.setPrice(285);
        flight2.setDirect(true);
        flight2.setCabinClass("Economy");
        flights.add(flight2);

        return flights;
    }
}

