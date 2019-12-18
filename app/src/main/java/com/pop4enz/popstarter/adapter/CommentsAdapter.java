package com.pop4enz.popstarter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.model.Comment;
import com.pop4enz.popstarter.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> comments = new ArrayList<>();

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setItems(List<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
        this.comments.sort((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        notifyDataSetChanged();
    }

    public void addItem(Comment comment) {
        this.comments.add(comment);
        this.comments.sort((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        notifyDataSetChanged();
    }

    public void clearItems() {
        comments.clear();
        notifyDataSetChanged();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView username;
        TextView content;
        TextView createdAt;
        ImageView image;

        CommentViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.commentName);
            username = itemView.findViewById(R.id.commentUsername);
            content = itemView.findViewById(R.id.commentContent);
            createdAt = itemView.findViewById(R.id.commentCreatedAt);
            image = itemView.findViewById(R.id.commentImage);
        }

        void bind(Comment comment) {
            name.setText(String.format("%s %s", comment
                    .getUser().getFirst_name(), comment.getUser().getLast_name()));
            username.setText(String.format("@%s", comment.getUser().getUsername()));
            content.setText(comment.getContent());
            Utils.loadImage(comment.getUser().getPicture(), image);
            createdAt.setText(Utils.getFormattedDate(comment.getCreatedAt()));
        }

    }

}
