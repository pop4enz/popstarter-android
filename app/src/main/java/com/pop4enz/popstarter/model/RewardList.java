package com.pop4enz.popstarter.model;

import java.io.Serializable;
import java.util.List;

public class RewardList implements Serializable {

    private List<Reward> rewards;

    public RewardList() {
    }

    public RewardList(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public void addReward(Reward reward) {
        this.rewards.add(reward);
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }
}
