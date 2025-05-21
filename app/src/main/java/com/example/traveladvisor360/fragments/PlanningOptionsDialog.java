package com.example.traveladvisor360.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.TripPlanningData;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class PlanningOptionsDialog extends DialogFragment {

    private RadioGroup tripTypeGroup;
    private MaterialCardView soloCardView;
    private MaterialCardView groupCardView;
    private MaterialButton btnNext;
    private MaterialButton btnCancel;
    private TextView titleText;
    private TripPlanningData tripData;
    private RadioButton rbSolo;
    private RadioButton rbGroup;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_trip_planning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize TripPlanningData
        tripData = TripPlanningData.getInstance();

        // Initialize views
        tripTypeGroup = view.findViewById(R.id.trip_type_group);
        soloCardView = view.findViewById(R.id.card_solo_trip);
        groupCardView = view.findViewById(R.id.card_group_trip);
        btnNext = view.findViewById(R.id.btn_next);
        btnCancel = view.findViewById(R.id.btn_cancel);
        titleText = view.findViewById(R.id.dialog_title);
        rbSolo = view.findViewById(R.id.rb_solo_trip);
        rbGroup = view.findViewById(R.id.rb_group_trip);

        // Set the title with an animation
        titleText.setAlpha(0f);
        titleText.animate().alpha(1f).setDuration(500).start();

        // Set up card selection behavior
        soloCardView.setOnClickListener(v -> {
            rbSolo.setChecked(true);
            rbGroup.setChecked(false);
            updateCardSelection();
            btnNext.setEnabled(true);
        });

        groupCardView.setOnClickListener(v -> {
            rbGroup.setChecked(true);
            rbSolo.setChecked(false);
            updateCardSelection();
            btnNext.setEnabled(true);
        });

        // Set up radio button click listeners
        rbSolo.setOnClickListener(v -> {
            rbSolo.setChecked(true);
            rbGroup.setChecked(false);
            updateCardSelection();
            btnNext.setEnabled(true);
        });

        rbGroup.setOnClickListener(v -> {
            rbGroup.setChecked(true);
            rbSolo.setChecked(false);
            updateCardSelection();
            btnNext.setEnabled(true);
        });

        // Initialize card states
        updateCardSelection();
        btnNext.setEnabled(false); // Initially disable next button

        btnNext.setOnClickListener(v -> {
            if (rbSolo.isChecked()) {
                tripData.setTripType("solo");
                navigateToDestinationSelectionSolo();
            } else if (rbGroup.isChecked()) {
                tripData.setTripType("group");
                navigateToDestinationSelectionGroup();
            } else {
                Toast.makeText(requireContext(), "Please select a trip type", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void updateCardSelection() {
        if (rbSolo.isChecked()) {
            soloCardView.setStrokeColor(getResources().getColor(R.color.colorAccent));
            soloCardView.setStrokeWidth(4);
            groupCardView.setStrokeColor(Color.LTGRAY);
            groupCardView.setStrokeWidth(1);
        } else if (rbGroup.isChecked()) {
            groupCardView.setStrokeColor(getResources().getColor(R.color.colorAccent));
            groupCardView.setStrokeWidth(4);
            soloCardView.setStrokeColor(Color.LTGRAY);
            soloCardView.setStrokeWidth(1);
        } else {
            soloCardView.setStrokeColor(Color.LTGRAY);
            soloCardView.setStrokeWidth(1);
            groupCardView.setStrokeColor(Color.LTGRAY);
            groupCardView.setStrokeWidth(1);
        }
    }

    private void navigateToDestinationSelectionSolo() {
        dismiss();
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_homeFragment_to_destinationBudgetFragment);
    }

    private void navigateToDestinationSelectionGroup() {
        dismiss();
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_homeFragment_to_destinationBudgetFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.9);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        }
    }
}