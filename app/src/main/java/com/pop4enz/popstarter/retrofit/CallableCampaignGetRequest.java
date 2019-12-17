package com.pop4enz.popstarter.retrofit;

import android.util.Log;

import com.pop4enz.popstarter.model.Campaign;

import java.util.concurrent.Callable;

import retrofit2.Call;

public class CallableCampaignGetRequest implements Callable {

    private int campaignId;

    public CallableCampaignGetRequest(int campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public Object call() {
        try {
            Call<Campaign> call = RetrofitService.getInstance()
                    .getJSONApi().getCampaignByID(campaignId);
            return call.execute().body();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return null;
    }


}
