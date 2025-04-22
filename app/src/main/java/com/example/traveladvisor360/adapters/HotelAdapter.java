package com.example.traveladvisor360.adapters;




// 3. HotelAdapter.java


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Hotel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private Context context;
    private List<Hotel> hotels = new ArrayList<>();
    private OnHotelClickListener listener;

    public HotelAdapter(Context context) {
        this.context = context;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
        notifyDataSetChanged();
    }

    public void setOnHotelClickListener(OnHotelClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Hotel hotel = hotels.get(position);

        holder.tvName.setText(hotel.getName());
        holder.tvAddress.setText(hotel.getAddress());
        holder.tvRating.setText(String.format(Locale.getDefault(), "%.1f", hotel.getRating()));
        holder.tvReviews.setText(String.format(Locale.getDefault(), "%d reviews", hotel.getReviewCount()));
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.0f", hotel.getPricePerNight()));
        holder.tvPerNight.setText("per night");

        Glide.with(context)
                .load(getImageResource(hotel.getImageUrl()))
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.ivHotel);

        setupAmenities(holder.amenitiesChipGroup, hotel.getAmenities());

        holder.btnSelect.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHotelSelect(hotel);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHotelClick(hotel);
            }
        });
    }

    private int getImageResource(String imageName) {
        int resourceId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());
        return resourceId != 0 ? resourceId : R.drawable.placeholder_image;
    }

    private void setupAmenities(ChipGroup chipGroup, List<String> amenities) {
        chipGroup.removeAllViews();
        int maxChips = 3; // Show only first 3 amenities

        for (int i = 0; i < Math.min(amenities.size(), maxChips); i++) {
            Chip chip = new Chip(context);
            chip.setText(amenities.get(i));
            chip.setChipBackgroundColorResource(R.color.tag_background);
            chip.setTextColor(context.getResources().getColor(R.color.primary));
            chipGroup.addView(chip);
        }

        if (amenities.size() > maxChips) {
            Chip moreChip = new Chip(context);
            moreChip.setText("+" + (amenities.size() - maxChips) + " more");
            moreChip.setChipBackgroundColorResource(R.color.tag_background);
            moreChip.setTextColor(context.getResources().getColor(R.color.primary));
            chipGroup.addView(moreChip);
        }
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public interface OnHotelClickListener {
        void onHotelClick(Hotel hotel);
        void onHotelSelect(Hotel hotel);
    }

    static class HotelViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView ivHotel;
        TextView tvName;
        TextView tvAddress;
        TextView tvRating;
        TextView tvReviews;
        TextView tvPrice;
        TextView tvPerNight;
        ChipGroup amenitiesChipGroup;
        MaterialButton btnSelect;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_hotel);
            ivHotel = itemView.findViewById(R.id.iv_hotel);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvReviews = itemView.findViewById(R.id.tv_reviews);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPerNight = itemView.findViewById(R.id.tv_per_night);
            amenitiesChipGroup = itemView.findViewById(R.id.amenities_chip_group);
            btnSelect = itemView.findViewById(R.id.btn_select);
        }
    }
}
