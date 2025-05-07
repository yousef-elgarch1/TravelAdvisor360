package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.CheminAdapter;
import com.example.traveladvisor360.models.Chemin;

import java.util.ArrayList;
import java.util.List;

public class CheminListFragment extends Fragment {

    private static final String ARG_TAB_POSITION = "tab_position";
    private int tabPosition;
    private RecyclerView recyclerView;
    private TextView emptyStateTextView;
    private CheminAdapter adapter;

    public static CheminListFragment newInstance(int tabPosition) {
        CheminListFragment fragment = new CheminListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabPosition = getArguments().getInt(ARG_TAB_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chemin_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        emptyStateTextView = view.findViewById(R.id.tv_empty_state);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load different data based on tab position
        List<Chemin> chemins = loadChemins(tabPosition);

        adapter = new CheminAdapter(chemins, cheminId -> {
            // Navigate to chemin details when a chemin is clicked
            Bundle args = new Bundle();
            args.putString("cheminId", cheminId);
            Navigation.findNavController(view).navigate(
                    R.id.action_cheminFragment_to_cheminDetailsFragment, args);
        });
        recyclerView.setAdapter(adapter);

        // Show empty state if no chemins
        if (chemins.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateTextView.setVisibility(View.GONE);
        }
    }

    private List<Chemin> loadChemins(int tabPosition) {
        // This is where you would load real data from your database or API
        // For now, let's create some sample data
        List<Chemin> chemins = new ArrayList<>();

        switch (tabPosition) {
            case 0: // À venir
                chemins.add(new Chemin(
                        "1",
                        "Weekend à Rome",
                        "Rome, Italie",
                        "5 mars 2025 - 8 mars 2025",
                        R.drawable.temples,
                        "voir_le_resume"));
                break;
            case 1: // En cours
                chemins.add(new Chemin(
                        "2",
                        "Escapade à Paris",
                        "Paris, France",
                        "15 mai 2025 - 22 mai 2025",
                        R.drawable.paris,
                        "voir_litineraire"));
                chemins.add(new Chemin(
                        "3",
                        "Aventure à Tokyo",
                        "Tokyo, Japon",
                        "1 juin 2025 - 15 juin 2025",
                        R.drawable.tokyo,
                        "voir_litineraire"));
                break;
            case 2: // Terminés
                // Add completed trips here
                break;
        }

        return chemins;
    }
}
