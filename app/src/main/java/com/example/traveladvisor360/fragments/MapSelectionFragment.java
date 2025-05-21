package com.example.traveladvisor360.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.traveladvisor360.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MapSelectionFragment extends BottomSheetDialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_IS_DESTINATION = "is_destination";

    private TextView titleTextView;
    private Button btnConfirm;
    private Button btnCancel;
    private ImageView mockMapView;
    private ImageView mapMarker;
    private EditText searchEditText;
    private Button btnLocation1;
    private Button btnLocation2;
    private Button btnLocation3;

    private float markerX = 0;
    private float markerY = 0;
    private boolean isDragging = false;

    private OnLocationSelectedListener locationListener;
    private boolean isDestination;

    public interface OnLocationSelectedListener {
        void onLocationSelected(String locationName, boolean isDestination);
    }

    public static MapSelectionFragment newInstance(String title, boolean isDestination) {
        MapSelectionFragment fragment = new MapSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putBoolean(ARG_IS_DESTINATION, isDestination);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_selection, container, false);

        // Initialize views
        titleTextView = view.findViewById(R.id.text_title);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        btnCancel = view.findViewById(R.id.btn_cancel);
        mockMapView = view.findViewById(R.id.mock_map_view);
        mapMarker = view.findViewById(R.id.map_marker);
        searchEditText = view.findViewById(R.id.edit_search);
        btnLocation1 = view.findViewById(R.id.btn_location_1);
        btnLocation2 = view.findViewById(R.id.btn_location_2);
        btnLocation3 = view.findViewById(R.id.btn_location_3);

        // Get arguments
        Bundle args = getArguments();
        if (args != null) {
            titleTextView.setText(args.getString(ARG_TITLE, "Select Location"));
            isDestination = args.getBoolean(ARG_IS_DESTINATION, true);
        }

        // Setup map interaction
        setupMapInteraction();

        // Setup buttons
        setupButtons();

        // Setup search
        setupSearch();

        return view;
    }

    private void setupMapInteraction() {
        mockMapView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDragging = true;
                    // No break here to fall through to ACTION_MOVE
                case MotionEvent.ACTION_MOVE:
                    if (isDragging) {
                        // Update marker position
                        updateMarkerPosition(event.getX(), event.getY());
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    isDragging = false;
                    return true;
            }
            return false;
        });
    }

    private void updateMarkerPosition(float x, float y) {
        // Calculate constrained positions to keep marker within map bounds
        float mapWidth = mockMapView.getWidth();
        float mapHeight = mockMapView.getHeight();

        // Keep marker within map boundaries (accounting for marker size)
        float markerWidth = mapMarker.getWidth();
        float markerHeight = mapMarker.getHeight();

        // If marker dimensions are not yet available, use default values
        if (markerWidth == 0) markerWidth = 48;
        if (markerHeight == 0) markerHeight = 48;

        // Center marker on touch point, but keep within map bounds
        float constrainedX = Math.max(markerWidth / 2, Math.min(x, mapWidth - markerWidth / 2));
        float constrainedY = Math.max(markerHeight / 2, Math.min(y, mapHeight - markerHeight / 2));

        // Update marker position
        markerX = constrainedX - mapMarker.getWidth() / 2;
        markerY = constrainedY - mapMarker.getHeight();

        // Apply position
        mapMarker.setX(markerX);
        mapMarker.setY(markerY);
    }

    private void setupButtons() {
        btnConfirm.setOnClickListener(v -> {
            if (locationListener != null) {
                // Get location name from search or generate one based on marker position
                String locationName = getLocationName();
                locationListener.onLocationSelected(locationName, isDestination);
            }
            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());

        // Setup popular location buttons
        btnLocation1.setOnClickListener(v -> {
            searchEditText.setText("New York");
            placeMarkerRandomly();
        });

        btnLocation2.setOnClickListener(v -> {
            searchEditText.setText("Paris");
            placeMarkerRandomly();
        });

        btnLocation3.setOnClickListener(v -> {
            searchEditText.setText("Tokyo");
            placeMarkerRandomly();
        });
    }

    private void placeMarkerRandomly() {
        // Get map dimensions
        int mapWidth = mockMapView.getWidth();
        int mapHeight = mockMapView.getHeight();

        if (mapWidth > 0 && mapHeight > 0) {
            // Generate random position within map bounds
            float x = (float) (Math.random() * mapWidth);
            float y = (float) (Math.random() * mapHeight);

            // Update marker position
            updateMarkerPosition(x, y);
        } else {
            // If map dimensions not available yet, use post delayed
            mockMapView.post(() -> placeMarkerRandomly());
        }
    }

    private void setupSearch() {
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // When search is pressed, move marker to a random position
                placeMarkerRandomly();

                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    private String getLocationName() {
        String searchText = searchEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(searchText)) {
            return searchText;
        }

        // If no search text, generate a mock location name based on marker position
        return "Selected Location";
    }

    public void setOnLocationSelectedListener(OnLocationSelectedListener listener) {
        this.locationListener = listener;
    }
}