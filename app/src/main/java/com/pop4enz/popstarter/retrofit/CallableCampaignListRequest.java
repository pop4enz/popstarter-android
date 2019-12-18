package com.pop4enz.popstarter.retrofit;

import android.util.Log;

import com.pop4enz.popstarter.model.MiniCampaign;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;

public class CallableCampaignListRequest implements Callable {

    public CallableCampaignListRequest() {
    }

    @Override
    public Object call() {
        try {
            Call<List<MiniCampaign>> call = RetrofitService.getInstance()
                    .getJSONApi().getAllCampaigns();
            return call.execute().body();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return null;
    }
}
