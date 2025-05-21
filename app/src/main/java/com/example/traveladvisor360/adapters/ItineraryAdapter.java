package com.example.traveladvisor360.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Itinerary;
import com.example.traveladvisor360.models.ItineraryDay;
import com.example.traveladvisor360.models.SavedItinerary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ItineraryViewHolder> {

    private Context context;
    private List<Itinerary> itineraries = new ArrayList<>();
    private List<SavedItinerary> savedItineraries = new ArrayList<>();
    private List<ItineraryDay> itineraryDays = new ArrayList<>();
    private OnItineraryClickListener listener;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private boolean isShowingItineraryDays = false;
    private boolean isShowingSavedItineraries = false;

    public ItineraryAdapter(Context context) {
        this.context = context;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
        this.isShowingItineraryDays = false;
        this.isShowingSavedItineraries = false;
        notifyDataSetChanged();
    }

    public void setSavedItineraries(List<SavedItinerary> savedItineraries) {
        this.savedItineraries = savedItineraries;
        this.isShowingItineraryDays = false;
        this.isShowingSavedItineraries = true;
        notifyDataSetChanged();
    }

    public void setItineraryDays(List<ItineraryDay> itineraryDays) {
        this.itineraryDays = itineraryDays;
        this.isShowingItineraryDays = true;
        this.isShowingSavedItineraries = false;
        notifyDataSetChanged();
    }

    public void setOnItineraryClickListener(OnItineraryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_itinerary, parent, false);
        return new ItineraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryViewHolder holder, int position) {
        if (isShowingItineraryDays) {
            bindItineraryDayView(holder, itineraryDays.get(position));
        } else if (isShowingSavedItineraries) {
            SavedItinerary itinerary = savedItineraries.get(position);
            holder.bind(itinerary);
        } else {
            Itinerary itinerary = itineraries.get(position);
            bindItineraryView(holder, itinerary);
        }
    }

    private void bindItineraryView(ItineraryViewHolder holder, Itinerary itinerary) {
        holder.destinationText.setText(itinerary.getDestination());
        holder.datesText.setText(String.format("%s - %s",
                dateFormat.format(itinerary.getStartDate()),
                dateFormat.format(itinerary.getEndDate())));
        holder.tripTypeText.setText(itinerary.isCollaborative() ? "Group" : "Solo");
        holder.budgetText.setText(String.format(Locale.getDefault(), "$%.2f", itinerary.getBudget()));
        
        // Hide unused views for regular itineraries
        holder.flightText.setVisibility(View.GONE);
        holder.hotelText.setVisibility(View.GONE);
        holder.activitiesText.setVisibility(View.GONE);
        holder.companionsTitle.setVisibility(View.GONE);
        holder.companionsText.setVisibility(View.GONE);

        holder.expandButton.setOnClickListener(v -> holder.toggleDetails());
    }

    private void bindItineraryDayView(ItineraryViewHolder holder, ItineraryDay day) {
        // Bind itinerary day view
        holder.destinationText.setText(String.format("Day %d - %s", day.getDayNumber(), day.getFormattedDate()));

        // Set day notes if available
        if (day.getNotes() != null && !day.getNotes().isEmpty()) {
            holder.activitiesText.setText(day.getNotes());
            holder.activitiesText.setVisibility(View.VISIBLE);
        } else {
            holder.activitiesText.setVisibility(View.GONE);
        }

        // Hide unused views
        holder.tripTypeText.setVisibility(View.GONE);
        holder.budgetText.setVisibility(View.GONE);
        holder.flightText.setVisibility(View.GONE);
        holder.hotelText.setVisibility(View.GONE);
        holder.companionsTitle.setVisibility(View.GONE);
        holder.companionsText.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (isShowingItineraryDays) {
            return itineraryDays.size();
        } else if (isShowingSavedItineraries) {
            return savedItineraries.size();
        } else {
            return itineraries.size();
        }
    }

    public interface OnItineraryClickListener {
        void onItineraryClick(Itinerary itinerary);
    }

    static class ItineraryViewHolder extends RecyclerView.ViewHolder {
        private TextView destinationText;
        private TextView datesText;
        private TextView tripTypeText;
        private TextView budgetText;
        private TextView flightText;
        private TextView hotelText;
        private TextView activitiesText;
        private TextView companionsTitle;
        private TextView companionsText;
        private ImageButton expandButton;
        private View divider;
        private View detailsLayout;

        public ItineraryViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationText = itemView.findViewById(R.id.destinationText);
            datesText = itemView.findViewById(R.id.datesText);
            tripTypeText = itemView.findViewById(R.id.tripTypeText);
            budgetText = itemView.findViewById(R.id.budgetText);
            flightText = itemView.findViewById(R.id.flightText);
            hotelText = itemView.findViewById(R.id.hotelText);
            activitiesText = itemView.findViewById(R.id.activitiesText);
            companionsTitle = itemView.findViewById(R.id.companionsTitle);
            companionsText = itemView.findViewById(R.id.companionsText);
            expandButton = itemView.findViewById(R.id.expandButton);
            divider = itemView.findViewById(R.id.divider);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);

            expandButton.setOnClickListener(v -> toggleDetails());
        }

        void bind(SavedItinerary itinerary) {
            destinationText.setText(itinerary.getDestinationCityName());
            datesText.setText(String.format("%s - %s",
                    dateFormat.format(itinerary.getStartDate()),
                    dateFormat.format(itinerary.getReturnDate())));
            tripTypeText.setText(itinerary.getTripType());

            budgetText.setText(String.format("%s %.2f", itinerary.getCurrency(), itinerary.getBudget()));
            flightText.setText(itinerary.getSelectedFlight());
            hotelText.setText(itinerary.getSelectedHotel());

            // Format activities
            StringBuilder activitiesBuilder = new StringBuilder();
            for (String activity : itinerary.getSelectedActivities()) {
                if (activitiesBuilder.length() > 0) {
                    activitiesBuilder.append("\n");
                }
                activitiesBuilder.append("• ").append(activity);
            }
            activitiesText.setText(activitiesBuilder.toString());

            // Handle companions visibility
            if ("group".equals(itinerary.getTripType()) && !itinerary.getCompanions().isEmpty()) {
                companionsTitle.setVisibility(View.VISIBLE);
                companionsText.setVisibility(View.VISIBLE);
                StringBuilder companionsBuilder = new StringBuilder();
                for (com.example.traveladvisor360.models.TravelCompanion companion : itinerary.getCompanions()) {
                    if (companionsBuilder.length() > 0) {
                        companionsBuilder.append("\n");
                    }
                    companionsBuilder.append("• ").append(companion.getName());
                }
                companionsText.setText(companionsBuilder.toString());
            } else {
                companionsTitle.setVisibility(View.GONE);
                companionsText.setVisibility(View.GONE);
            }
        }

        void toggleDetails() {
            boolean isExpanded = detailsLayout.getVisibility() == View.VISIBLE;
            detailsLayout.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            divider.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            expandButton.setRotation(isExpanded ? 0 : 180);
        }
    }
}