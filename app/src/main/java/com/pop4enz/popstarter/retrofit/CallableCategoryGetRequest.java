package com.pop4enz.popstarter.retrofit;

import com.pop4enz.popstarter.model.CampaignCategory;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;

public class CallableCategoryGetRequest implements Callable {

    private String token;

    public CallableCategoryGetRequest(String token) {
        this.token = token;
    }

    @Override
    public Object call() throws Exception {
        Call<List<CampaignCategory>> call = RetrofitService.getInstance()
                .getApiRequests().getCategories(token);
        return call.execute().body();
    }
}
