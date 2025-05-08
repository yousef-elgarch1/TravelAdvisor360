package com.example.traveladvisor360.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.TravelCompanion;

import java.util.List;

public class CompanionSummaryAdapter extends RecyclerView.Adapter<CompanionSummaryAdapter.CompanionViewHolder> {

    private List<TravelCompanion> companions;

    public CompanionSummaryAdapter(List<TravelCompanion> companions) {
        this.companions = companions;
    }

    @NonNull
    @Override
    public CompanionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_summary_companion, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return companions.size();
    }

    static class CompanionViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView emailText;
        TextView preferencesText;

        public CompanionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_companion_name);
            emailText = itemView.findViewById(R.id.text_companion_email);
            preferencesText = itemView.findViewById(R.id.text_companion_preferences);
        }
    }
}