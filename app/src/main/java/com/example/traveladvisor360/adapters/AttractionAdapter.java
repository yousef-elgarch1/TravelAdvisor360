package com.example.traveladvisor360.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.example.traveladvisor360.R;

import java.util.ArrayList;
import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {

    private Context context;
    private List<String> attractions = new ArrayList<>();
    private OnAttractionClickListener listener;

    public AttractionAdapter(Context context) {
        this.context = context;
    }

    public void setAttractions(List<String> attractions) {
        this.attractions = attractions;
        notifyDataSetChanged();
    }

    public void setOnAttractionClickListener(OnAttractionClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AttractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attraction, parent, false);
        return new AttractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionViewHolder holder, int position) {
        String attraction = attractions.get(position);
        holder.tvAttractionName.setText(attraction);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAttractionClick(attraction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }

    public interface OnAttractionClickListener {
        void onAttractionClick(String attraction);
    }

    static class AttractionViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tvAttractionName;

        public AttractionViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_attraction);
            tvAttractionName = itemView.findViewById(R.id.tv_attraction_name);
        }}}
