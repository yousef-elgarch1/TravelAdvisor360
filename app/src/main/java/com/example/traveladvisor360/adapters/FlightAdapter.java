package com.example.traveladvisor360.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Flight;
import com.example.traveladvisor360.models.FlightOption;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.text.NumberFormat;
import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {


    private OnFlightSelectedListener listener;

    private List<FlightOption> flights;
    public FlightAdapter(List<FlightOption> flights) {
        this.flights = flights;
    }

    public void setFlights(List<Flight> flights) {
        notifyDataSetChanged();
    }

    public interface OnFlightSelectedListener {
        void onFlightSelected(FlightOption flight);
    }



    public void setOnFlightSelectedListener(OnFlightSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        FlightOption flight = flights.get(position);

        // Set flight details
        holder.airlineText.setText(flight.getAirline());
        holder.flightNumberText.setText(flight.getFlightNumber());
        holder.departureText.setText(String.format("%s %s", flight.getDepartureAirport(), flight.getDepartureTime()));
        holder.arrivalText.setText(String.format("%s %s", flight.getArrivalAirport(), flight.getArrivalTime()));

        // Set price
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        holder.priceText.setText(currencyFormat.format(flight.getPrice()));

        // Set class and stops
        holder.classChip.setText(flight.getTravelClass());
        holder.stopsChip.setText(flight.getStops() == 0 ? "Non-stop" : flight.getStops() + " stop(s)");

        // Update selection state
        if (flight.isSelected()) {
            holder.flightCard.setStrokeColor(holder.itemView.getContext().getResources().getColor(R.color.colorAccent));
            holder.flightCard.setStrokeWidth(4);
        } else {
            holder.flightCard.setStrokeColor(holder.itemView.getContext().getResources().getColor(R.color.gray_medium));
            holder.flightCard.setStrokeWidth(1);
        }

        // Set click listener
        holder.flightCard.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFlightSelected(flight);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    static class FlightViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView flightCard;
        TextView airlineText;
        TextView flightNumberText;
        TextView departureText;
        TextView arrivalText;
        TextView priceText;
        Chip classChip;
        Chip stopsChip;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightCard = itemView.findViewById(R.id.card_flight);
            airlineText = itemView.findViewById(R.id.text_airline);
            flightNumberText = itemView.findViewById(R.id.text_flight_number);
            departureText = itemView.findViewById(R.id.text_departure);
            arrivalText = itemView.findViewById(R.id.text_arrival);
            priceText = itemView.findViewById(R.id.text_price);
            classChip = itemView.findViewById(R.id.chip_class);
            stopsChip = itemView.findViewById(R.id.chip_stops);
        }
    }
}