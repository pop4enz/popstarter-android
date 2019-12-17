package com.pop4enz.popstarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.pop4enz.popstarter.fragments.CommentsFragment;
import com.pop4enz.popstarter.fragments.DescriptionFragment;
import com.pop4enz.popstarter.model.Campaign;
import com.pop4enz.popstarter.model.Comment;
import com.pop4enz.popstarter.model.CommentList;
import com.pop4enz.popstarter.retrofit.CallableCampaignGetRequest;
import com.pop4enz.popstarter.retrofit.CallableCommentsGetRequest;
import com.pop4enz.popstarter.tabs.CampaignTabsPagerAdapter;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity
        implements DescriptionFragment.OnFragmentInteractionListener,
        CommentsFragment.OnFragmentInteractionListener {

    public static final String DEFAULT_IMAGE = "https://sun9-14.userapi.com/c854120/v854120120/179b7e/5MAug30XaaM.jpg";
    CarouselView carouselView;

    private TextView title;
    private TextView category;
    private TextView goal;
    private TextView goalProgressTv;

    private CampaignTabsPagerAdapter tabsPagerAdapter;

    private ProgressBar goalProgress;

    private StompClient stompClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();

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
                "ws://popstarter.herokuapp.com/apiws/websocket");
        stompClient.connect();

        Observable.fromCallable(new CallableCampaignGetRequest(1))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<Campaign>) this::mapCampaignValues).isDisposed();
        Observable.fromCallable(new CallableCommentsGetRequest(1))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<CommentList>) this::mapComments).isDisposed();

        stompClient.topic("/api/campaign/1/comments").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(topicMessage
                -> addCommentToRv(gson.fromJson(topicMessage.getPayload(),
                Comment.class))).isDisposed();

    }

    private void mapCampaignValues(Campaign campaign) {
        if (campaign != null) {
            title.setText(campaign.getTitle());
            category.setText(campaign.getCategoryName());
            goal.setText(format(getString(R.string.GOAL_TEXT),
                    campaign.getCurrentMoney(), campaign.getGoal()));
            tabsPagerAdapter.setDescription(campaign.getDescription());
            float progress = campaign.getCurrentMoney() / campaign.getGoal();
            goalProgressTv.setText(format("%.1f %s", progress, getString(R.string.GOALTV_TEXT)));
            goalProgress.setProgress(Math.round(progress));
            if (!campaign.getImages().isEmpty()) {
                carouselView.setImageListener((position, imageView)
                        -> loadImageInto(campaign.getImages().get(position).getLink(), imageView));
                carouselView.setPageCount(campaign.getImages().size());
            } else {
                carouselView.setImageListener((position, imageView)
                        -> loadImageInto(DEFAULT_IMAGE, imageView));
                carouselView.setPageCount(1);
            }
        }
    }

    private void mapComments(CommentList comments) {
        if (comments != null) {
            tabsPagerAdapter.setComments(comments);
        }
    }

    private void addCommentToRv(Comment comment) {
        tabsPagerAdapter.addComment(comment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stompClient.disconnect();
    }

    private void loadImageInto(String uri, ImageView imageView) {
        Picasso.get().load(uri).into(imageView);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
