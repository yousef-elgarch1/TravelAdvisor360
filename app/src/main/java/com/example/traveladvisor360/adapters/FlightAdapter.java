package com.example.traveladvisor360.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Flight;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {

    private Context context;
    private List<Flight> flights = new ArrayList<>();
    private OnFlightClickListener listener;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public FlightAdapter(Context context) {
        this.context = context;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
        notifyDataSetChanged();
    }

    public void setOnFlightClickListener(OnFlightClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flights.get(position);

        holder.tvAirline.setText(flight.getAirline());
        holder.tvDepartureTime.setText(timeFormat.format(flight.getDepartureTime()));
        holder.tvArrivalTime.setText(timeFormat.format(flight.getArrivalTime()));
        holder.tvDepartureCity.setText(flight.getDepartureCity());
        holder.tvArrivalCity.setText(flight.getArrivalCity());
        holder.tvDuration.setText(formatDuration(flight.getDuration()));
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.0f", flight.getPrice()));
        holder.tvPerPerson.setText("per person");

        if (flight.isDirect()) {
            holder.tvDirectFlight.setText("Direct");
        } else {
            holder.tvDirectFlight.setText("1 Stop");
        }

        // Set airline logo (you would need to add airline logo resources)
        holder.ivAirlineLogo.setImageResource(getAirlineLogo(flight.getAirlineCode()));

        holder.btnSelect.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFlightSelect(flight);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFlightClick(flight);
            }
        });
    }

    private String formatDuration(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        return String.format(Locale.getDefault(), "%dh %dm", hours, mins);
    }

    private int getAirlineLogo(String airlineCode) {
        // You would need to add airline logo resources and map them here
        return R.drawable.ic_flight; // Default flight icon
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    public interface OnFlightClickListener {
        void onFlightClick(Flight flight);
        void onFlightSelect(Flight flight);
    }

    static class FlightViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView ivAirlineLogo;
        TextView tvAirline;
        TextView tvDepartureTime;
        TextView tvArrivalTime;
        TextView tvDepartureCity;
        TextView tvArrivalCity;
        TextView tvDuration;
        TextView tvDirectFlight;
        TextView tvPrice;
        TextView tvPerPerson;
        MaterialButton btnSelect;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_flight);
            ivAirlineLogo = itemView.findViewById(R.id.iv_airline_logo);
            tvAirline = itemView.findViewById(R.id.tv_airline);
            tvDepartureTime = itemView.findViewById(R.id.tv_departure_time);
            tvArrivalTime = itemView.findViewById(R.id.tv_arrival_time);
            tvDepartureCity = itemView.findViewById(R.id.tv_departure_city);
            tvArrivalCity = itemView.findViewById(R.id.tv_arrival_city);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            tvDirectFlight = itemView.findViewById(R.id.tv_direct_flight);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPerPerson = itemView.findViewById(R.id.tv_per_person);
            btnSelect = itemView.findViewById(R.id.btn_select);
        }
    }
}