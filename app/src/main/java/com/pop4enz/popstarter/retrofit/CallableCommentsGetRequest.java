package com.pop4enz.popstarter.retrofit;

import android.util.Log;

import com.pop4enz.popstarter.model.Comment;
import com.pop4enz.popstarter.model.CommentList;

import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;

public class CallableCommentsGetRequest implements Callable {

    private int campaignId;

    public CallableCommentsGetRequest(int campaignId) {
        this.campaignId = campaignId;
    }

    @Override
    public Object call() throws Exception {
        try {
            Call<List<Comment>> call = RetrofitService.getInstance()
                    .getApiRequests().getCommentsByID(campaignId);

            return new CommentList(call.execute().body());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return null;
    }
}
