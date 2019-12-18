package com.pop4enz.popstarter.retrofit;

import android.util.Log;

import com.pop4enz.popstarter.model.Reward;
import com.pop4enz.popstarter.model.RewardList;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;

public class CallableRewardsGetRequest implements Callable {

    private int campaignId;

    public CallableRewardsGetRequest(int campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public Object call() throws Exception {
        try {
            Call<List<Reward>> call = RetrofitService.getInstance()
                    .getJSONApi().getRewardsByID(campaignId);

            return new RewardList(call.execute().body());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return null;
    }
}
