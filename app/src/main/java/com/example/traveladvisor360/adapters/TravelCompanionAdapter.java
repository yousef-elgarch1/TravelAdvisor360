package com.example.traveladvisor360.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.TravelCompanion;

import java.util.List;

public class TravelCompanionAdapter extends RecyclerView.Adapter<TravelCompanionAdapter.CompanionViewHolder> {

    private List<TravelCompanion> companions;
    private OnCompanionRemovedListener listener;

    public interface OnCompanionRemovedListener {
        void onCompanionRemoved(int position);
    }

    public TravelCompanionAdapter(List<TravelCompanion> companions) {
        this.companions = companions;
    }

    public void setOnCompanionRemovedListener(OnCompanionRemovedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompanionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel_companion, parent, false);
        return new CompanionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanionViewHolder holder, int position) {
        TravelCompanion companion = companions.get(position);
        holder.nameText.setText(companion.getName());
        holder.emailText.setText(companion.getEmail());

        // Show preferences if available
        String preferences = companion.getPreferences();
        if (preferences != null && !preferences.isEmpty()) {
            holder.preferencesText.setVisibility(View.VISIBLE);
            holder.preferencesText.setText(preferences);
        } else {
            holder.preferencesText.setVisibility(View.GONE);
        }

        // Set click listener for delete button
        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCompanionRemoved(holder.getAdapterPosition());
            }
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return companions.size();
    }

    static class CompanionViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView emailText;
        TextView preferencesText;
        ImageButton deleteButton;

        public CompanionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_companion_name);
            emailText = itemView.findViewById(R.id.text_companion_email);
            preferencesText = itemView.findViewById(R.id.text_companion_preferences);
            deleteButton = itemView.findViewById(R.id.btn_delete_companion);
        }
    }
}