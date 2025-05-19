package com.example.traveladvisor360.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.GeoapifyResponse;
import com.example.traveladvisor360.models.TripPlanningData;
import com.example.traveladvisor360.utils.AirportUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DestinationBudgetFragment extends Fragment {

    private AutoCompleteTextView editDestination;
    private EditText editBudget;
    private Spinner spinnerCurrency;
    private EditText editStartDate;
    private EditText editReturnDate;
    private ArrayAdapter<String> destinationAdapter;

    private com.example.traveladvisor360.network.GeoapifyService geoapifyService;
    private static final String GEOAPIFY_BASE_URL = "https://api.geoapify.com/";
    private static final String GEOAPIFY_API_KEY = "3a2057294ce64a67a85b49d2016412e1";
    private AutoCompleteTextView editDeparture;
    private ArrayAdapter<String> departureAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination_budget, container, false);

        editDestination = view.findViewById(R.id.edit_destination);
        editBudget = view.findViewById(R.id.edit_budget);
        spinnerCurrency = view.findViewById(R.id.spinner_currency);
        editStartDate = view.findViewById(R.id.edit_start_date);
        editReturnDate = view.findViewById(R.id.edit_return_date);

        destinationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        editDestination.setAdapter(destinationAdapter);

        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.currencies,
                android.R.layout.simple_spinner_item
        );
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(currencyAdapter);

        editDeparture = view.findViewById(R.id.edit_departure);
        departureAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        editDeparture.setAdapter(departureAdapter);

        Retrofit geoapifyRetrofit = new Retrofit.Builder()
                .baseUrl(GEOAPIFY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        geoapifyService = geoapifyRetrofit.create(com.example.traveladvisor360.network.GeoapifyService.class);

        editDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    fetchCitySuggestions(s.toString(), destinationAdapter);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        editDeparture.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    fetchCitySuggestions(s.toString(), departureAdapter);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        editStartDate.setOnClickListener(v -> showDatePickerDialog(editStartDate));
        editReturnDate.setOnClickListener(v -> showDatePickerDialog(editReturnDate));

        Button btnNext = view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> {
            String destinationInput = editDestination.getText().toString().trim();
            String departureInput = editDeparture.getText().toString().trim();

            String[] destParts = destinationInput.split(",");
            String[] depParts = departureInput.split(",");

            if (destParts.length < 2 || depParts.length < 2) {
                Toast.makeText(requireContext(), "Please select valid destination and departure.", Toast.LENGTH_SHORT).show();
                return;
            }
            String destCity = destParts[0].trim();
            String destCountry = destParts[1].trim();
            String depCity = depParts[0].trim();
            String depCountry = depParts[1].trim();

            String destIata = AirportUtils.findIataCode(requireContext(), destCity, destCountry);
            String depIata = AirportUtils.findIataCode(requireContext(), depCity, depCountry);

            if (destIata == null || depIata == null) {
                Toast.makeText(requireContext(), "Could not find IATA code for one of the cities.", Toast.LENGTH_SHORT).show();
                return;
            }

            saveTripDataAndNavigate(destIata, depIata, destCity, v);
        });

        return view;
    }

    private void showDatePickerDialog(final EditText targetEditText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    targetEditText.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void fetchCitySuggestions(String query, ArrayAdapter<String> adapter) {
        geoapifyService.autocompleteCities(query, GEOAPIFY_API_KEY)
                .enqueue(new Callback<GeoapifyResponse>() {
                    @Override
                    public void onResponse(Call<GeoapifyResponse> call, Response<GeoapifyResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<String> suggestions = new ArrayList<>();
                            for (GeoapifyResponse.Feature feature : response.body().features) {
                                String city = feature.properties.city;
                                String country = feature.properties.country;
                                if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(country)) {
                                    suggestions.add(city + ", " + country);
                                }
                            }
                            adapter.clear();
                            adapter.addAll(suggestions);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(Call<GeoapifyResponse> call, Throwable t) { }
                });
    }

    private void saveTripDataAndNavigate(String destIata, String depIata, String destCity, View v) {
        TripPlanningData tripData = TripPlanningData.getInstance();
        tripData.setDestination(destIata);
        tripData.setDeparture(depIata);
        tripData.setDestinationCityName(destCity); // <-- This line ensures city name is saved

        String budgetStr = editBudget.getText().toString().trim();
        double budget = 0;
        if (!budgetStr.isEmpty()) {
            try {
                budget = Double.parseDouble(budgetStr);
            } catch (NumberFormatException ignored) { }
        }
        tripData.setBudget(budget);
        tripData.setCurrency(spinnerCurrency.getSelectedItem().toString());
        tripData.setStartDate(editStartDate.getText().toString().trim());
        tripData.setReturnDate(editReturnDate.getText().toString().trim());

        Navigation.findNavController(v).navigate(R.id.action_destinationBudgetFragment_to_flightsFragment);
    }
}