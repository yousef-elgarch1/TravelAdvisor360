package com.example.traveladvisor360.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.traveladvisor360.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewCheminFragment extends Fragment {

    private TextInputEditText etTitle;
    private TextInputEditText etDestination;
    private TextInputEditText etStartDate;
    private TextInputEditText etEndDate;
    private Button btnSave;

    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.FRENCH);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_chemin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        etTitle = view.findViewById(R.id.et_title);
        etDestination = view.findViewById(R.id.et_destination);
        etStartDate = view.findViewById(R.id.et_start_date);
        etEndDate = view.findViewById(R.id.et_end_date);
        btnSave = view.findViewById(R.id.btn_save);

        // Set up back navigation
        toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        // Set up date pickers
        setupDatePickers();

        // Set up save button
        btnSave.setOnClickListener(v -> {
            if (validateForm()) {
                saveNewChemin();
                Navigation.findNavController(view).navigateUp();
            }
        });
    }

    private void setupDatePickers() {
        // Start date picker
        etStartDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                startDateCalendar.set(Calendar.YEAR, year);
                startDateCalendar.set(Calendar.MONTH, month);
                startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etStartDate.setText(dateFormat.format(startDateCalendar.getTime()));
            },
                    startDateCalendar.get(Calendar.YEAR),
                    startDateCalendar.get(Calendar.MONTH),
                    startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // End date picker
        etEndDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                endDateCalendar.set(Calendar.YEAR, year);
                endDateCalendar.set(Calendar.MONTH, month);
                endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etEndDate.setText(dateFormat.format(endDateCalendar.getTime()));
            },
                    endDateCalendar.get(Calendar.YEAR),
                    endDateCalendar.get(Calendar.MONTH),
                    endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (etTitle.getText().toString().trim().isEmpty()) {
            etTitle.setError(getString(R.string.required_field));
            isValid = false;
        }

        if (etDestination.getText().toString().trim().isEmpty()) {
            etDestination.setError(getString(R.string.required_field));
            isValid = false;
        }

        if (etStartDate.getText().toString().trim().isEmpty()) {
            etStartDate.setError(getString(R.string.required_field));
            isValid = false;
        }

        if (etEndDate.getText().toString().trim().isEmpty()) {
            etEndDate.setError(getString(R.string.required_field));
            isValid = false;
        }

        // Check if end date is after start date
        if (isValid && startDateCalendar.after(endDateCalendar)) {
            etEndDate.setError(getString(R.string.end_date_error));
            isValid = false;
        }

        return isValid;
    }

    private void saveNewChemin() {
        // Here you would save the new chemin to your database or shared preferences
        Toast.makeText(requireContext(), R.string.chemin_saved, Toast.LENGTH_SHORT).show();
    }
}