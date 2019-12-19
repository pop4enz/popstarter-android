package com.pop4enz.popstarter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.model.Campaign;
import com.pop4enz.popstarter.model.CampaignCategory;
import com.pop4enz.popstarter.retrofit.CallableCampaignGetRequest;
import com.pop4enz.popstarter.retrofit.CallableCategoryGetRequest;
import com.pop4enz.popstarter.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EditCampaignActivity extends NavigationActivity {

    private Integer campaignId;
    private Disposable campaignDisposable;
    private Disposable categoryDisposable;
    private SharedPreferences storage;

    private EditText campaignTitleET;
    private EditText campaignDescriptionET;
    private EditText campaignImageET1;
    private EditText campaignGoalET;
    private EditText campaignEndDateET;
    ArrayAdapter<String> adapter;
    private Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_campaign);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        campaignId = (int) intent.getLongExtra("campaignId", 0);
        storage = this.getStorage();

        categorySpinner = findViewById(R.id.categorySpinner);
        campaignTitleET = findViewById(R.id.campaignTitleET);
        campaignDescriptionET = findViewById(R.id.campaignDescriptionET);
        campaignImageET1 = findViewById(R.id.campaignImageET1);
        campaignGoalET = findViewById(R.id.campaignGoalET);
        campaignEndDateET = findViewById(R.id.campaignGoalET);

        categoryDisposable = Observable.fromCallable(new CallableCategoryGetRequest(Utils
                .buildToken(storage.getString(getString(R.string.TOKEN), null))))
        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        .subscribe((Consumer<List<CampaignCategory>>) this::mapCategories);
        if (campaignId != 0) {
            campaignDisposable = Observable.fromCallable(new CallableCampaignGetRequest(campaignId))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe((Consumer<Campaign>) this::mapCampaignValues);
        }
    }

    private void mapCategories(List<CampaignCategory> categories) {
        List<String> categoryNames = new ArrayList<>();
        for (CampaignCategory category : categories) {
            categoryNames.add(category.getName());
        }
        adapter = new ArrayAdapter<>(EditCampaignActivity.this,
                android.R.layout.simple_spinner_item, categoryNames);
        categorySpinner.setAdapter(adapter);
//        categorySpinner.getSelectedItem()
    }

    private void mapCampaignValues(Campaign campaign) {
        campaignTitleET.setText(campaign.getTitle());
        campaignDescriptionET.setText(campaign.getDescription());
        campaignImageET1.setText(campaign.getImages().get(0).getLink());
        campaignGoalET.setText(String.valueOf(campaign.getGoal()));
        campaignEndDateET.setText(String.valueOf(campaign.getExpiresAt()));
    }

}
