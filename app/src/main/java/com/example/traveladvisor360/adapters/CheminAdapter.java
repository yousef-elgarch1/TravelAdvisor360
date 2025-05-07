package com.example.traveladvisor360.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.interfaces.OnCheminClickListener;
import com.example.traveladvisor360.models.Chemin;

import java.util.List;

public class CheminAdapter extends RecyclerView.Adapter<CheminAdapter.CheminViewHolder> {

    private List<Chemin> chemins;
    private OnCheminClickListener listener;

    public CheminAdapter(List<Chemin> chemins, OnCheminClickListener listener) {
        this.chemins = chemins;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CheminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chemin, parent, false);
        return new CheminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheminViewHolder holder, int position) {
        Chemin chemin = chemins.get(position);
        Context context = holder.itemView.getContext();

        holder.destinationTitle.setText(chemin.getTitle());
        holder.destinationLocation.setText(chemin.getLocation());
        holder.destinationDates.setText(chemin.getDateRange());
        holder.destinationImage.setImageResource(chemin.getImageResId());

        // Set the details button text based on the buttonTextKey
        int buttonTextResId;
        if ("voir_le_resume".equals(chemin.getButtonTextKey())) {
            buttonTextResId = R.string.voir_le_resume;
        } else {
            buttonTextResId = R.string.voir_litineraire;
        }
        holder.viewDetailsButton.setText(buttonTextResId);

        // Set click listeners
        holder.editButton.setOnClickListener(v -> {
            // Handle edit click here
        });

        holder.viewDetailsButton.setOnClickListener(v -> {
            listener.onCheminClick(chemin.getId());
        });
    }

    @Override
    public int getItemCount() {
        return chemins.size();
    }

    static class CheminViewHolder extends RecyclerView.ViewHolder {

        ImageView destinationImage;
        TextView destinationTitle;
        TextView destinationLocation;
        TextView destinationDates;
        ImageButton editButton;
        Button viewDetailsButton;

        public CheminViewHolder(@NonNull View itemView) {
            super(itemView);

            destinationImage = itemView.findViewById(R.id.img_destination);
            destinationTitle = itemView.findViewById(R.id.tv_destination_title);
            destinationLocation = itemView.findViewById(R.id.tv_destination_location);
            destinationDates = itemView.findViewById(R.id.tv_destination_dates);
            editButton = itemView.findViewById(R.id.btn_edit);
            viewDetailsButton = itemView.findViewById(R.id.btn_view_details);
        }
    }
}