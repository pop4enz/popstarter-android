package com.pop4enz.popstarter.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.adapter.RewardsAdapter;
import com.pop4enz.popstarter.model.Reward;
import com.pop4enz.popstarter.model.RewardList;

import java.util.Objects;

public class RewardsFragment extends Fragment {

    private static final String REWARDS = "rewards";

    private RewardList rewards;

    private RecyclerView rewardsRv;

    private RewardsAdapter rewardsAdapter = new RewardsAdapter();

    private OnFragmentInteractionListener mListener;

    public RewardsFragment() {
    }

    public static RewardsFragment newInstance(RewardList rewards) {
        RewardsFragment fragment = new RewardsFragment();
        Bundle args = new Bundle();
        args.putSerializable(REWARDS, rewards);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rewards = (RewardList) getArguments().getSerializable(REWARDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rewards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rewardsRv = Objects.requireNonNull(getView()).findViewById(R.id.rewardsRv);
        rewardsRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rewardsRv.setAdapter(rewardsAdapter);
        if (rewards != null) {
            rewardsAdapter.setItems(rewards.getRewards());
        }
    }

    public void setRewards(RewardList rewards) {
        Bundle args = new Bundle();
        this.rewards = rewards;
        args.putSerializable(REWARDS, rewards);
        this.setArguments(args);
        rewardsAdapter.setItems(rewards.getRewards());
    }

    public void addReward(Reward reward) {
        Bundle args = new Bundle();
        this.rewards.addReward(reward);
        args.putSerializable(REWARDS, rewards);
        this.setArguments(args);
        rewardsAdapter.addItem(reward);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
