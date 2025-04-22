package com.example.traveladvisor360.adapters;

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
import com.example.traveladvisor360.models.TravelTip;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class TravelTipsAdapter extends RecyclerView.Adapter<TravelTipsAdapter.TravelTipViewHolder> {

    private Context context;
    private List<TravelTip> travelTips = new ArrayList<>();
    private OnTravelTipClickListener listener;

    public TravelTipsAdapter(Context context) {
        this.context = context;
    }

    public void setTravelTips(List<TravelTip> travelTips) {
        this.travelTips = travelTips;
        notifyDataSetChanged();
    }

    public void setOnTravelTipClickListener(OnTravelTipClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TravelTipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_travel_tip, parent, false);
        return new TravelTipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelTipViewHolder holder, int position) {
        TravelTip tip = travelTips.get(position);

        holder.tvTitle.setText(tip.getTitle());
        holder.tvDescription.setText(tip.getDescription());
        holder.chipCategory.setText(tip.getCategory());

        Glide.with(context)
                .load(getImageResource(tip.getImageUrl()))
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(holder.ivTravelTip);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTravelTipClick(tip);
            }
        });
    }

    private int getImageResource(String imageName) {
        int resourceId = context.getResources().getIdentifier(
                imageName, "drawable", context.getPackageName());
        return resourceId != 0 ? resourceId : R.drawable.placeholder_image;
    }

    @Override
    public int getItemCount() {
        return travelTips.size();
    }

    public interface OnTravelTipClickListener {
        void onTravelTipClick(TravelTip tip);
    }

    static class TravelTipViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView ivTravelTip;
        TextView tvTitle;
        TextView tvDescription;
        Chip chipCategory;

        public TravelTipViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_travel_tip);
            ivTravelTip = itemView.findViewById(R.id.iv_travel_tip);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            chipCategory = itemView.findViewById(R.id.chip_category);
        }
    }
}