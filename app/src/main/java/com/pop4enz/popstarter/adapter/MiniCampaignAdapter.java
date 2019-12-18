package com.pop4enz.popstarter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.model.MiniCampaign;
import com.pop4enz.popstarter.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MiniCampaignAdapter extends RecyclerView.Adapter<MiniCampaignAdapter.MiniCampaignViewHolder> {

    private List<MiniCampaign> campaigns = new ArrayList<>();
    private OnCampaignListener onCampaignListener;

    public MiniCampaignAdapter(OnCampaignListener onCampaignListener) {
        this.onCampaignListener = onCampaignListener;
    }

    @NonNull
    @Override
    public MiniCampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.campaign_item, parent, false);
        return new MiniCampaignViewHolder(view, onCampaignListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MiniCampaignViewHolder holder, int position) {
        holder.bind(campaigns.get(position));
    }

    @Override
    public int getItemCount() {
        return campaigns.size();
    }

    public long getItemId(int position) {
        return campaigns.get(position).getId();
    }

    public void setItems(List<MiniCampaign> campaigns) {
        this.campaigns = campaigns;
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.campaigns.clear();
        notifyDataSetChanged();
    }

    class MiniCampaignViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public static final String CAMPAIGN_DESCRIPTION = "Category: %s \n" +
                "\n%.0f$ pledged out of %.0f$ goal. \n" +
                "\nAverage rating: %.1f";
        ImageView image;
        TextView title;
        TextView description;
        TextView createdAt;

        OnCampaignListener onCampaignListener;

        MiniCampaignViewHolder(View itemView, OnCampaignListener onCampaignListener) {
            super(itemView);
            image = itemView.findViewById(R.id.miniCampaignImage);
            title = itemView.findViewById(R.id.miniCampaignTitle);
            description = itemView.findViewById(R.id.miniCampaignDescription);
            createdAt = itemView.findViewById(R.id.miniCampaignCreatedAt);
            this.onCampaignListener = onCampaignListener;

            itemView.setOnClickListener(this);
        }

        void bind(MiniCampaign campaign) {
            Utils.loadImage(campaign.getImage(), image);
            title.setText(String.format("%s", campaign.getTitle()));
            description.setText(String.format(Locale.getDefault(), CAMPAIGN_DESCRIPTION,
                    campaign.getCategoryName(), campaign.getCurrentMoney(),
                    campaign.getGoal(), campaign.getAvgrating()));
            createdAt.setText(Utils.getFormattedDate(campaign.getCreatedAt()));
        }

        @Override
        public void onClick(View v) {
            onCampaignListener.onCampaignClick(getAdapterPosition());
        }
    }

    public interface OnCampaignListener {
        void onCampaignClick(int position);
    }

}
