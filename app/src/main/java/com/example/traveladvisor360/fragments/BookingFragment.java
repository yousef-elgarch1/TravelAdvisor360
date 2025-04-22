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
import com.example.traveladvisor360.R;
import com.google.android.material.textfield.TextInputEditText;

public class BookingFragment extends Fragment {

    private TextView tvTitle;
    private TextView tvPrice;
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etPhone;
    private TextInputEditText etSpecialRequests;
    private Button btnConfirmBooking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupClickListeners();

        // Load booking details from arguments
        loadBookingDetails();
    }

    private void initViews(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvPrice = view.findViewById(R.id.tv_price);
        etName = view.findViewById(R.id.et_name);
        etEmail = view.findViewById(R.id.et_email);
        etPhone = view.findViewById(R.id.et_phone);
        etSpecialRequests = view.findViewById(R.id.et_special_requests);
        btnConfirmBooking = view.findViewById(R.id.btn_confirm_booking);
    }

    private void setupClickListeners() {
        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void loadBookingDetails() {
        if (getArguments() != null) {
            String title = getArguments().getString("title", "");
            double price = getArguments().getDouble("price", 0.0);

            tvTitle.setText(title);
            tvPrice.setText(String.format("$%.2f", price));
        }
    }

    private void confirmBooking() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String specialRequests = etSpecialRequests.getText().toString().trim();

        // Validate input
        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone is required");
            return;
        }

        // Process booking
        // This would typically call an API to complete the booking
    }
}

