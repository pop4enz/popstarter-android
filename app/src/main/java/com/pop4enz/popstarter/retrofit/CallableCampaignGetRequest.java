package com.pop4enz.popstarter.retrofit;

import android.os.SystemClock;
import android.util.Log;

import com.pop4enz.popstarter.model.Campaign;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallableCampaignGetRequest implements Callable {

    private Campaign campaign;

    public CallableCampaignGetRequest() {
    }

    @Override
    public Object call() throws Exception {
        RetrofitService.getInstance()
                .getJSONApi().getCampaignByID(1)
                .enqueue(new Callback<Campaign>() {
                    @Override
                    public void onResponse(Call<Campaign> call, Response<Campaign> response) {
                        campaign = response.body();
                        }
                    @Override
                    public void onFailure(Call<Campaign> call, Throwable t) {
                        Log.e("ERROR", t.getMessage());
                    }
                });
        SystemClock.sleep(1500);
        return campaign;
    }


}
