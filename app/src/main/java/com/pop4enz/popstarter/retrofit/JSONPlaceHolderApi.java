package com.pop4enz.popstarter.retrofit;

import com.pop4enz.popstarter.model.Campaign;
import com.pop4enz.popstarter.model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi {
    @GET("campaign/{id}")
    Call<Campaign> getCampaignByID(@Path("id") int id);
    @GET("campaign/{id}/comments")
    Call<List<Comment>> getCommentsByID(@Path("id") int id);
}
