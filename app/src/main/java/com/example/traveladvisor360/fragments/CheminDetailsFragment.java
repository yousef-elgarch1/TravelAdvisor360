package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.traveladvisor360.R;

public class CheminDetailsFragment extends Fragment {

    private String cheminId;
    private TextView titleTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cheminId = getArguments().getString("cheminId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chemin_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        titleTextView = view.findViewById(R.id.tv_chemin_title);

        // Set up back navigation
        toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });

        // Load chemin details based on the ID
        loadCheminDetails();
    }

    private void loadCheminDetails() {
        // In a real app, you would load the details from a database or API
        // For now, we'll just set title based on the ID

        String title;
        switch (cheminId) {
            case "1":
                title = "Weekend à Rome";
                break;
            case "2":
                title = "Escapade à Paris";
                break;
            case "3":
                title = "Aventure à Tokyo";
                break;
            default:
                title = getString(R.string.chemin_details);
                break;
        }

        titleTextView.setText(title);
    }
}