package com.example.traveladvisor360.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.example.traveladvisor360.R;

public class PlanningOptionsDialog extends DialogFragment {

    private RadioButton rbSoloTrip;
    private RadioButton rbGroupTrip;
    private Button btnNext;
    private Button btnBack;

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

        rbSoloTrip = view.findViewById(R.id.rb_solo_trip);
        rbGroupTrip = view.findViewById(R.id.rb_group_trip);
        btnNext = view.findViewById(R.id.btn_next);
        btnBack = view.findViewById(R.id.btn_back);

        btnNext.setOnClickListener(v -> {
            if (rbSoloTrip.isChecked()) {
                navigateToSoloPlanning();
            } else if (rbGroupTrip.isChecked()) {
                navigateToGroupPlanning();
            } else {
                Toast.makeText(requireContext(), "Please select a trip type", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> dismiss());
    }

    private void navigateToSoloPlanning() {
        dismiss();
        Bundle args = new Bundle();
        args.putString("trip_type", "solo");
        Navigation.findNavController(requireParentFragment().requireView())
                .navigate(R.id.action_homeFragment_to_tripPlanningActivity, args);
    }

    private void navigateToGroupPlanning() {
        dismiss();
        Bundle args = new Bundle();
        args.putString("trip_type", "group");
        Navigation.findNavController(requireParentFragment().requireView())
                .navigate(R.id.action_homeFragment_to_tripPlanningActivity, args);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}