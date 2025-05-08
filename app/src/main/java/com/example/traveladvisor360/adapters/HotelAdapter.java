package com.example.traveladvisor360.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Hotel;
import com.example.traveladvisor360.models.HotelOption;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private List<HotelOption> hotels;
    private OnHotelSelectedListener listener;


    public void setHotels(List<Hotel> hotels) {

        notifyDataSetChanged();
    }

    public interface OnHotelSelectedListener {
        void onHotelSelected(HotelOption hotel);
    }

    public HotelAdapter(List<HotelOption> hotels) {
        this.hotels = hotels;
    }

    public void setOnHotelSelectedListener(OnHotelSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        HotelOption hotel = hotels.get(position);

        // Set hotel details
        holder.nameText.setText(hotel.getName());
        holder.locationText.setText(hotel.getLocation());
        holder.descriptionText.setText(hotel.getDescription());

        // Set rating
        holder.ratingText.setText(String.format("%.1f/5.0", hotel.getRating()));
        holder.ratingBar.setRating((float) hotel.getRating());

        // Set stars
        StringBuilder starsBuilder = new StringBuilder();
        for (int i = 0; i < hotel.getStars(); i++) {
            starsBuilder.append("★");
        }
        holder.starsText.setText(starsBuilder.toString());

        // Set price
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        holder.priceText.setText(String.format("%s per night", currencyFormat.format(hotel.getPricePerNight())));

        // Update selection state
        if (hotel.isSelected()) {
            holder.hotelCard.setStrokeColor(holder.itemView.getContext().getResources().getColor(R.color.colorAccent));
            holder.hotelCard.setStrokeWidth(4);
        } else {
            holder.hotelCard.setStrokeColor(holder.itemView.getContext().getResources().getColor(R.color.gray_medium));
            holder.hotelCard.setStrokeWidth(1);
        }

        // Set click listener
        holder.hotelCard.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHotelSelected(hotel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    static class HotelViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView hotelCard;
        TextView nameText;
        TextView locationText;
        TextView descriptionText;
        TextView ratingText;
        RatingBar ratingBar;
        TextView starsText;
        TextView priceText;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelCard = itemView.findViewById(R.id.card_hotel);
            nameText = itemView.findViewById(R.id.text_hotel_name);
            locationText = itemView.findViewById(R.id.text_location);
            descriptionText = itemView.findViewById(R.id.text_description);
            ratingText = itemView.findViewById(R.id.text_rating);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            starsText = itemView.findViewById(R.id.text_stars);
            priceText = itemView.findViewById(R.id.text_price);
        }
    }
}