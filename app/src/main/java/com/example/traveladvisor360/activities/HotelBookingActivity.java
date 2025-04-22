package com.example.traveladvisor360.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.HotelAdapter;
import com.example.traveladvisor360.models.Hotel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class HotelBookingActivity extends AppCompatActivity {

    private TextInputEditText etDestination;
    private TextInputEditText etCheckIn;
    private TextInputEditText etCheckOut;
    private Spinner spinnerGuests;
    private Spinner spinnerRooms;
    private RangeSlider priceRangeSlider;
    private RecyclerView rvHotels;
    private HotelAdapter hotelAdapter;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_booking);

        initViews();
        setupRecyclerView();
        setupDatePickers();
        setupSearchButton();
    }

    private void initViews() {
        etDestination = findViewById(R.id.et_destination);
        etCheckIn = findViewById(R.id.et_check_in);
        etCheckOut = findViewById(R.id.et_check_out);
        spinnerGuests = findViewById(R.id.spinner_guests);
        spinnerRooms = findViewById(R.id.spinner_rooms);
        priceRangeSlider = findViewById(R.id.price_range_slider);
        rvHotels = findViewById(R.id.rv_hotels);
        btnSearch = findViewById(R.id.btn_search);
    }

    private void setupRecyclerView() {
        rvHotels.setLayoutManager(new LinearLayoutManager(this));
        hotelAdapter = new HotelAdapter(this);
        rvHotels.setAdapter(hotelAdapter);
    }

    private void setupDatePickers() {
        etCheckIn.setOnClickListener(v -> showDatePicker(etCheckIn));
        etCheckOut.setOnClickListener(v -> showDatePicker(etCheckOut));
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
        btnSearch.setOnClickListener(v -> searchHotels());
    }

    private void searchHotels() {
        // Get search parameters
        String destination = etDestination.getText().toString();
        String checkIn = etCheckIn.getText().toString();
        String checkOut = etCheckOut.getText().toString();
        int guests = spinnerGuests.getSelectedItemPosition() + 1;
        int rooms = spinnerRooms.getSelectedItemPosition() + 1;

        // Mock hotel data
        List<Hotel> hotels = createMockHotels();
        hotelAdapter.setHotels(hotels);
    }

    private List<Hotel> createMockHotels() {
        List<Hotel> hotels = new ArrayList<>();

        Hotel hotel1 = new Hotel();
        hotel1.setId("1");
        hotel1.setName("Hotel Plaza Athénée");
        hotel1.setAddress("25 Avenue Montaigne, 8th arr., Paris");
        hotel1.setRating(4.8f);
        hotel1.setPricePerNight(450);
        hotel1.setDescription("Luxury hotel located on the prestigious Avenue Montaigne, with Eiffel Tower views and elegant rooms.");
        hotel1.setAmenities(java.util.Arrays.asList("Free WiFi", "Restaurant", "Fitness center", "Spa"));
        hotels.add(hotel1);

        Hotel hotel2 = new Hotel();
        hotel2.setId("2");
        hotel2.setName("Hôtel Le Marais");
        hotel2.setAddress("12 Rue des Archives, 4th arr., Paris");
        hotel2.setRating(4.3f);
        hotel2.setPricePerNight(220);
        hotel2.setDescription("Boutique hotel in the trendy Le Marais district, offering contemporary rooms in a historic building.");
        hotel2.setAmenities(java.util.Arrays.asList("Free WiFi", "Restaurant", "Bar"));
        hotels.add(hotel2);

        return hotels;
    }
}