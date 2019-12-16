package com.pop4enz.popstarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pop4enz.popstarter.fragments.CommentsFragment;
import com.pop4enz.popstarter.fragments.DescriptionFragment;
import com.pop4enz.popstarter.model.Campaign;
import com.pop4enz.popstarter.retrofit.CallableCampaignGetRequest;
import com.pop4enz.popstarter.tabs.CampaignTabsPagerAdapter;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class MainActivity extends AppCompatActivity implements DescriptionFragment.OnFragmentInteractionListener, CommentsFragment.OnFragmentInteractionListener {

    CarouselView carouselView;

    private TextView title;
    private TextView category;
    private TextView goal;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private ProgressBar goalProgress;

    private TextView name;
    private TextView username;
    private TextView content;
    private TextView createdAt;
    private ImageView picture;

    private StompClient stompClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.campaignTabsVp);
        CampaignTabsPagerAdapter tabsPagerAdapter = new CampaignTabsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);
        tabLayout = findViewById(R.id.campaignTabsLayout);
        tabLayout.setupWithViewPager(viewPager);
        carouselView = findViewById(R.id.carouselView);
        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        goal = findViewById(R.id.goal);
        goalProgress = findViewById(R.id.goalProgress);

//        name = findViewById(R.id.name);
//        username = findViewById(R.id.username);
//        content = findViewById(R.id.content);
//        createdAt = findViewById(R.id.createdAt);
//        picture = findViewById(R.id.picture);

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://popstarter.herokuapp.com/apiws/websocket");
        stompClient.connect();

        Observable.fromCallable(new CallableCampaignGetRequest())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<Campaign>) campaign -> {
                    title.setText(campaign.getTitle());
                    category.setText(campaign.getCategoryName());
                    goal.setText(String.format("%f pledged out of %f goal", campaign.getCurrentMoney(), campaign.getGoal()));
                    tabsPagerAdapter.setDescription(campaign.getDescription());
                    goalProgress.setProgress(40);
                    if (!campaign.getImages().isEmpty()) {
                        carouselView.setImageListener((position, imageView)
                                -> loadImageInto(campaign.getImages().get(position).getLink(), imageView));
                        carouselView.setPageCount(campaign.getImages().size());
                    }
                });

        stompClient.topic("/api/campaign/2/comments").subscribe(topicMessage -> {
            Log.d("WEBSOCKET", topicMessage.getPayload());
        });

//        RetrofitService.getInstance()
//                .getJSONApi()
//                .getCommentsByID(1)
//                .enqueue(new Callback<List<Comment>>() {
//                    @Override
//                    public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
//                        List<Comment> comments = response.body();
//
//                        name.setText(String.format("%s %s", comments.get(0).getUser().getFirst_name(), comments.get(0).getUser().getLast_name()));
//                        username.setText(String.format("@%s", comments.get(0).getUser().getUsername()));
//                        content.setText(comments.get(0).getContent());
//                        createdAt.setText(comments.get(0).getCreatedAt());
//                        loadImage(comments.get(0).getUser().getPicture());
//                    }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stompClient.disconnect();
    }

    private void loadImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            loadImageInto(imagePath, picture);
        } else {
            loadImageInto("https://sun9-14.userapi.com/c854120/v854120120/179b7e/5MAug30XaaM.jpg", picture);
        }
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
