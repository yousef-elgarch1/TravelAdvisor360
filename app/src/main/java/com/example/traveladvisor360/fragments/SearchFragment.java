package com.example.traveladvisor360.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.adapters.DestinationAdapter;
import com.example.traveladvisor360.models.Destination;
import com.example.traveladvisor360.utils.ApiCallback;
import com.example.traveladvisor360.utils.ApiClient;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class SearchFragment extends Fragment {

    private TextInputEditText etSearch;
    private RecyclerView rvSearchResults;
    private DestinationAdapter destinationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.et_search);
        rvSearchResults = view.findViewById(R.id.rv_search_results);

        setupRecyclerView();

        String searchQuery = getArguments() != null ? getArguments().getString("search_query") : "";
        if (!searchQuery.isEmpty()) {
            etSearch.setText(searchQuery);
            performSearch(searchQuery);
        }
    }

    private void setupRecyclerView() {
        rvSearchResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        destinationAdapter = new DestinationAdapter(requireContext());
        rvSearchResults.setAdapter(destinationAdapter);
    }

    private void performSearch(String query) {
        ApiClient.getInstance().searchDestinations(query, new ApiCallback<List<Destination>>() {
            @Override
            public void onSuccess(List<Destination> result) {
                requireActivity().runOnUiThread(() -> {
                    destinationAdapter.setDestinations(result);
                });
            }

            @Override
            public void onError(String error) {
                // Handle error
            }
        });
    }
}
