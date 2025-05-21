package com.example.traveladvisor360.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveladvisor360.R;
import com.example.traveladvisor360.models.Comment;
import com.example.traveladvisor360.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context) {
        this.context = context;
        this.comments = new ArrayList<>();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        // Set user info
        holder.tvUserName.setText(comment.getUserName());

        if (comment.getCreatedAt() != null) {
            holder.tvCommentTime.setText(TimeUtils.getTimeAgo(comment.getCreatedAt()));
        }

        // Load user photo
        if (comment.getUserPhotoUrl() != null && !comment.getUserPhotoUrl().isEmpty()) {
            Glide.with(context)
                    .load(comment.getUserPhotoUrl())
                    .placeholder(R.drawable.placeholder_profile)
                    .error(R.drawable.placeholder_profile)
                    .into(holder.ivUserPhoto);
        }

        // Set comment content
        holder.tvCommentContent.setText(comment.getContent());

        // Set like count
        if (comment.getLikeCount() > 0) {
            holder.tvLikeCount.setText(String.valueOf(comment.getLikeCount()));
            holder.tvLikeCount.setVisibility(View.VISIBLE);
        } else {
            holder.tvLikeCount.setVisibility(View.GONE);
        }

        // Update like button state
        updateLikeButton(holder, comment);

        // Set click listeners
        holder.btnLike.setOnClickListener(v -> {
            comment.toggleLike();
            updateLikeButton(holder, comment);

            if (comment.getLikeCount() > 0) {
                holder.tvLikeCount.setText(String.valueOf(comment.getLikeCount()));
                holder.tvLikeCount.setVisibility(View.VISIBLE);
            } else {
                holder.tvLikeCount.setVisibility(View.GONE);
            }
        });

        holder.btnReply.setOnClickListener(v -> {
            // Reply functionality would be implemented here
            // For demonstration, we'll do nothing
        });
    }

    private void updateLikeButton(CommentViewHolder holder, Comment comment) {
        if (comment.isLiked()) {
            holder.btnLike.setText("Liked");
            holder.btnLike.setTextColor(context.getResources().getColor(R.color.like_active, null));
        } else {
            holder.btnLike.setText("Like");
            holder.btnLike.setTextColor(context.getResources().getColor(R.color.secondary_text, null));
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivUserPhoto;
        TextView tvUserName;
        TextView tvCommentTime;
        TextView tvCommentContent;
        TextView btnLike;
        TextView btnReply;
        TextView tvLikeCount;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserPhoto = itemView.findViewById(R.id.iv_user_photo);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvCommentTime = itemView.findViewById(R.id.tv_comment_time);
            tvCommentContent = itemView.findViewById(R.id.tv_comment_content);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnReply = itemView.findViewById(R.id.btn_reply);
            tvLikeCount = itemView.findViewById(R.id.tv_like_count);
        }
    }
}