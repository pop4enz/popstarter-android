package com.pop4enz.popstarter.activity;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.fragments.CommentsFragment;
import com.pop4enz.popstarter.fragments.DescriptionFragment;
import com.pop4enz.popstarter.fragments.RewardsFragment;
import com.pop4enz.popstarter.model.Campaign;
import com.pop4enz.popstarter.model.Comment;
import com.pop4enz.popstarter.model.CommentList;
import com.pop4enz.popstarter.model.RewardList;
import com.pop4enz.popstarter.retrofit.CallableCampaignGetRequest;
import com.pop4enz.popstarter.retrofit.CallableCommentsGetRequest;
import com.pop4enz.popstarter.retrofit.CallableRewardsGetRequest;
import com.pop4enz.popstarter.tabs.CampaignTabsPagerAdapter;
import com.pop4enz.popstarter.utils.Utils;
import com.synnapps.carouselview.CarouselView;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

import static java.lang.String.format;

public class CampaignActivity extends NavigationActivity
        implements DescriptionFragment.OnFragmentInteractionListener,
        CommentsFragment.OnFragmentInteractionListener,
        RewardsFragment.OnFragmentInteractionListener {

    public static final String WS_PATH = "ws://popstarter.herokuapp.com/apiws/websocket";
    public static final String API_WS_TOPIC = "/api/campaign/%d/comments";
    CarouselView carouselView;

    private TextView title;
    private TextView category;
    private TextView goal;
    private TextView goalProgressTv;
    private Boolean isAuthor = false;

    private Button backButton;

    private CampaignTabsPagerAdapter tabsPagerAdapter;

    private ProgressBar goalProgress;

    private StompClient stompClient;
    private Disposable wsDisposable;
    private Disposable lifecycleDisposable;
    private Disposable commentDisposable;
    private Disposable campaignDisposable;
    private Disposable rewardDisposable;

    private Gson gson;

    private Integer campaignId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_campaign);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        campaignId = (int) intent.getLongExtra("campaignId", 0);

        if (campaignId == 0) {
            Utils.Toast(this, Utils.ERROR);
            this.finish();
        }

        gson = new Gson();

        backButton = findViewById(R.id.backCampaignButton);
//        backButton.setVisibility(GONE);

        ViewPager viewPager = findViewById(R.id.campaignTabsVp);
        tabsPagerAdapter = new CampaignTabsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.campaignTabsLayout);
        tabLayout.setupWithViewPager(viewPager);

        carouselView = findViewById(R.id.carouselView);
        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        goal = findViewById(R.id.goal);
        goalProgressTv = findViewById(R.id.goalProgressTv);
        goalProgress = findViewById(R.id.goalProgress);

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP,
                WS_PATH);
        subscribeToWs();

        campaignDisposable = Observable.fromCallable(new CallableCampaignGetRequest(campaignId))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<Campaign>) this::mapCampaignValues);
        commentDisposable = Observable.fromCallable(new CallableCommentsGetRequest(campaignId))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<CommentList>) this::mapComments);
        rewardDisposable = Observable.fromCallable(new CallableRewardsGetRequest(campaignId))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<RewardList>) this::mapRewards);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wsLifecycleDispose();
        campaignRequestDispose();
        commentRequestDispose();
        wsDispose();
        stompClient.disconnect();
    }

    private void mapCampaignValues(Campaign campaign) {
        if (campaign != null) {
            title.setText(campaign.getTitle());
            category.setText(campaign.getCategoryName());
            goal.setText(format(getString(R.string.GOAL_TEXT),
                    campaign.getCurrentMoney(), campaign.getGoal()));
            tabsPagerAdapter.setDescription(campaign.getDescription());
            float progress = campaign.getCurrentMoney() / campaign.getGoal() * 100;
            goalProgressTv.setText(format(Locale.getDefault(), "%.1f %s", progress, getString(R.string.GOALTV_TEXT)));
            goalProgress.setProgress(Math.round(progress));
            if (!campaign.getImages().isEmpty()) {
                carouselView.setImageListener((position, imageView)
                        -> Utils.loadImageInto(campaign.getImages().get(position).getLink(), imageView));
                carouselView.setPageCount(campaign.getImages().size());
            } else {
                carouselView.setImageListener((position, imageView)
                        -> Utils.loadImageInto(null, imageView));
                carouselView.setPageCount(1);
            }
        }
        campaignRequestDispose();
    }

    private void mapComments(CommentList comments) {
        if (comments != null) {
            tabsPagerAdapter.setComments(comments);
        }
        commentRequestDispose();
    }

    private void mapRewards(RewardList rewards) {
        if (rewards != null) {
            tabsPagerAdapter.setRewards(rewards);
        }
        rewardsRequestDispose();
    }

    private void subscribeToWs() {
        stompClient.connect();
        wsLifecycleDispose();
        lifecycleDisposable = stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(Utils.TAG, "Stomp connection opened");
                    break;

                case ERROR:
                    Log.e(Utils.TAG, "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.d(Utils.TAG, "Stomp connection closed");
                    subscribeToWs();
                    break;
            }
        });
        wsDispose();
        wsDisposable = stompClient
                .topic(String.format(Locale.getDefault(), API_WS_TOPIC, campaignId)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(topicMessage
                        -> addCommentToRv(gson.fromJson(topicMessage.getPayload(),
                        Comment.class)), throwable
                        -> Log.e(Utils.TAG, "Error in WS ", throwable));
    }

    private void addCommentToRv(Comment comment) {
        tabsPagerAdapter.addComment(comment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void commentRequestDispose() {
        if (commentDisposable != null) {
            commentDisposable.dispose();
        }
    }

    private void campaignRequestDispose() {
        if (campaignDisposable != null) {
            campaignDisposable.dispose();
        }
    }

    private void wsDispose() {
        if (wsDisposable != null) {
            wsDisposable.dispose();
        }
    }

    private void wsLifecycleDispose() {
        if (lifecycleDisposable != null) {
            lifecycleDisposable.dispose();
        }
    }

    private void rewardsRequestDispose() {
        if (rewardDisposable != null) {
            rewardDisposable.dispose();
        }
    }

}
