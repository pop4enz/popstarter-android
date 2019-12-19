package com.pop4enz.popstarter.retrofit;

import com.pop4enz.popstarter.model.ApiResponse;
import com.pop4enz.popstarter.model.Campaign;
import com.pop4enz.popstarter.model.CampaignCategory;
import com.pop4enz.popstarter.model.Comment;
import com.pop4enz.popstarter.model.CreateCampaignRequest;
import com.pop4enz.popstarter.model.LoginRequest;
import com.pop4enz.popstarter.model.MiniCampaign;
import com.pop4enz.popstarter.model.Reward;
import com.pop4enz.popstarter.model.SignUpRequest;
import com.pop4enz.popstarter.model.TokenResponse;
import com.pop4enz.popstarter.model.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiRequests {

    String AUTHORIZATION = "Authorization";

    @GET("campaign/{id}")
    Call<Campaign> getCampaignByID(@Path("id") int id);

    @GET("campaign/{id}/comments")
    Call<List<Comment>> getCommentsByID(@Path("id") int id);

    @GET("campaign/{id}/rewards")
    Call<List<Reward>> getRewardsByID(@Path("id") int id);

    @GET("campaign/all")
    Call<List<MiniCampaign>> getAllCampaigns();

    @POST("auth/signin")
    Call<TokenResponse> signInRequest(@Body LoginRequest loginRequest);

    @POST("auth/signup")
    Call<ApiResponse> signUpRequest(@Body SignUpRequest signUpRequset);

    @GET("user/me")
    Call<UserInfo> getUserInfo(@Header(AUTHORIZATION) String authHeader);

    @GET("campaign/categories")
    Call<List<CampaignCategory>> getCategories(@Header(AUTHORIZATION) String authHeader);

    @POST("campaign/create")
    Call<Void> createCampaign(@Header("Authorization") String authHeader,
                              @Body CreateCampaignRequest request);

    @PATCH("campaign/update/{id}")
    Call<Void> updateCampaign(@Header(AUTHORIZATION) String authHeader,
                                               @Path("id") Integer campaignId,
                              @Body CreateCampaignRequest request);
}
