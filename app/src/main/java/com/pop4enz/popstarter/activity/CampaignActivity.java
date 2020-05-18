package com.pop4enz.popstarter.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.fragments.CommentsFragment;
import com.pop4enz.popstarter.fragments.DescriptionFragment;
import com.pop4enz.popstarter.fragments.RewardsFragment;
import com.pop4enz.popstarter.fragments.backCampaignDialogFragment;
import com.pop4enz.popstarter.model.Campaign;
import com.pop4enz.popstarter.model.Comment;
import com.pop4enz.popstarter.model.CommentList;
import com.pop4enz.popstarter.model.RewardList;
import com.pop4enz.popstarter.model.SupportRequest;
import com.pop4enz.popstarter.payload.CommentAddRequestDTO;
import com.pop4enz.popstarter.retrofit.CallableCampaignGetRequest;
import com.pop4enz.popstarter.retrofit.CallableCommentsGetRequest;
import com.pop4enz.popstarter.retrofit.CallableRewardsGetRequest;
import com.pop4enz.popstarter.retrofit.RetrofitService;
import com.pop4enz.popstarter.tabs.CampaignTabsPagerAdapter;
import com.pop4enz.popstarter.utils.Utils;
import com.synnapps.carouselview.CarouselView;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

import static java.lang.String.format;

public class CampaignActivity extends NavigationActivity
        implements DescriptionFragment.OnFragmentInteractionListener,
        CommentsFragment.OnFragmentInteractionListener,
        RewardsFragment.OnFragmentInteractionListener,
        View.OnClickListener {

    public static final String WS_PATH = "ws://popstarter.herokuapp.com/apiws/websocket";
    public static final String API_WS_TOPIC = "/api/campaign/%d/comments";
    public static final String SUPPORT_SUCCESS = "You supported this campaign successfully";
    public static final String DELETE_SUCCESS = "You delete this campaign successfully!";
    public static final String COMMENT_SUCCESS = "Your comment added successfully!";
    public static final String USER_MUST_LOGIN = "Log in to leave comments! :)";
    private CarouselView carouselView;

    private TextView title;
    private TextView category;
    private TextView goal;
    private TextView goalProgressTv;
    private float campaignGoal;
    private Integer creatorId = 0;

    private Button editButton;
    private Button deleteButton;
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
        deleteButton = findViewById(R.id.deleteCampaign);
        editButton = findViewById(R.id.editCampaign);
        deleteButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

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
        rewardsRequestDispose();
        wsDispose();
        stompClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.isUser) {
            backButton.setVisibility(View.VISIBLE);
            tabsPagerAdapter.setAddCommentVisibility(View.VISIBLE);
            setCreatorButtonsVisible(creatorId);
        } else {
            tabsPagerAdapter.setAddCommentVisibility(View.GONE);
            backButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }

    }

    private void mapCampaignValues(Campaign campaign) {
        if (campaign != null) {
            creatorId = campaign.getCreator().getId();
            setCreatorButtonsVisible(creatorId);
            title.setText(campaign.getTitle());
            category.setText(campaign.getCategoryName());
            campaignGoal = campaign.getGoal();
            mapMoney(campaign.getCurrentMoney());
            tabsPagerAdapter.setDescription(campaign.getDescription());
            if (!campaign.getImages().isEmpty()) {
                carouselView.setImageListener((position, imageView)
                        -> Utils.loadImageInto(campaign.getImages().get(position)
                        .getLink(), imageView));
                carouselView.setPageCount(campaign.getImages().size());
            } else {
                carouselView.setImageListener((position, imageView)
                        -> Utils.loadImageInto(null, imageView));
                carouselView.setPageCount(1);
            }
        }
        campaignRequestDispose();
    }

    private void setCreatorButtonsVisible(Integer id) {
        if(getStorage()
                .getInt(USER_ID, 0) == id) {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
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
                .topic(String.format(Locale.getDefault(), API_WS_TOPIC, campaignId))
                .subscribeOn(Schedulers.io())
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

    private void mapMoney(float money) {
        goal.setText(format(getString(R.string.GOAL_TEXT),
                money, campaignGoal));
        float progress = money / campaignGoal * 100;
        goalProgressTv.setText(format(Locale.getDefault(), "%.1f %s", progress,
                getString(R.string.GOALTV_TEXT)));
        goalProgress.setProgress(Math.round(progress));
    }

    public void supportCampaign(String value) {
        float val = Float.parseFloat(value);
        if (this.isUser && val < 5000) {
            RetrofitService.getInstance().getApiRequests().supportCampaign(this.getToken(),
                    campaignId, new SupportRequest(val))
                    .enqueue(new Callback<Float>() {
                @Override
                public void onResponse(@NonNull Call<Float> call,
                                       @NonNull Response<Float> response) {
                    if (response.isSuccessful()) {
                        float money;
                        money = response.body() != null ? response.body() : 0;
                        Utils.Toast(CampaignActivity.this, SUPPORT_SUCCESS);
                        mapMoney(money);
                    } else {
                        Utils.Toast(CampaignActivity.this, Utils.ERROR);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Float> call,
                                      @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Utils.Toast(this,
                    "You can't donate so much money for one transaction!");
        }
    }

    public void addComment(String value) {
        if (isUser) {
            CommentAddRequestDTO requestDTO = new CommentAddRequestDTO(value);
            RetrofitService.getInstance().getApiRequests()
                    .addComment(this.getToken(), campaignId, requestDTO)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if (response.isSuccessful()) {
                                Utils.Toast(CampaignActivity.this, COMMENT_SUCCESS);
                            } else {
                                Utils.Toast(CampaignActivity.this, Utils.ERROR);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            t.printStackTrace();
                        }
                    });
        } else {
            Utils.Toast(this, USER_MUST_LOGIN);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backCampaignButton:
                createSupportDialog();
                break;
            case R.id.editCampaign:
                handleEditClick();
                break;
            case R.id.deleteCampaign:
                handleDeleteClick();
                break;
        }
    }

    private void handleEditClick() {
        Intent intent = new Intent(this, EditCampaignActivity.class);
        intent.putExtra("campaignId", campaignId);
        startActivity(intent);
    }

    private void createSupportDialog() {
        FragmentManager fm = getSupportFragmentManager();
        backCampaignDialogFragment newFragment = new backCampaignDialogFragment();
        newFragment.show(fm, null);
    }

    private void handleDeleteClick() {
        RetrofitService.getInstance().getApiRequests().deleteCampaign(this.getToken(), campaignId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Utils.Toast(CampaignActivity.this, DELETE_SUCCESS);
                            Intent intent = new Intent(CampaignActivity.this,
                                    HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
