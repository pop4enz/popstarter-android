package com.pop4enz.popstarter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.model.Reward;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardViewHolder> {

    private List<Reward> rewards = new ArrayList<>();

    @NonNull
    @Override
    public RewardsAdapter.RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_item, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsAdapter.RewardViewHolder holder, int position) {
        holder.bind(rewards.get(position));
    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    public void setItems(List<Reward> rewards) {
        this.rewards.clear();
        this.rewards.addAll(rewards);
        notifyDataSetChanged();
    }

    public void addItem(Reward reward) {
        this.rewards.add(reward);
        notifyDataSetChanged();
    }

    public void clearItems() {
        rewards.clear();
        notifyDataSetChanged();
    }

    class RewardViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        TextView price;

        RewardViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rewardNameTv);
            description = itemView.findViewById(R.id.rewardDescriptionTv);
            price = itemView.findViewById(R.id.rewardPriceTv);
        }

        void bind(Reward reward) {
            name.setText(String.format("%s", reward.getName()));
            description.setText(String.format("%s", reward.getDescription()));
            price.setText(String.format(Locale.getDefault(), "%.1f $", reward.getPrice()));
        }

    }

}
