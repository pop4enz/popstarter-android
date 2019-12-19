package com.pop4enz.popstarter.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.model.Campaign;
import com.pop4enz.popstarter.model.CampaignCategory;
import com.pop4enz.popstarter.model.CreateCampaignRequest;
import com.pop4enz.popstarter.retrofit.ApiRequests;
import com.pop4enz.popstarter.retrofit.CallableCampaignGetRequest;
import com.pop4enz.popstarter.retrofit.CallableCategoryGetRequest;
import com.pop4enz.popstarter.retrofit.RetrofitService;
import com.pop4enz.popstarter.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCampaignActivity extends NavigationActivity implements View.OnClickListener {

    public static final String DEFAULT_VIDEO = "https://www.youtube.com/watch?v=XrmEns3sE60";
    private Integer campaignId;
    private Disposable campaignDisposable;
    private Disposable categoryDisposable;

    private EditText campaignTitleET;
    private EditText campaignDescriptionET;
    private EditText campaignImageET1;
    private EditText campaignGoalET;
    private TextView campaignEndDateET;
    private Spinner categorySpinner;

    private Calendar dateAndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit_campaign);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        campaignId = (int) intent.getLongExtra("campaignId", 0);

        categorySpinner = findViewById(R.id.categorySpinner);
        campaignTitleET = findViewById(R.id.campaignTitleET);
        campaignDescriptionET = findViewById(R.id.campaignDescriptionET);
        campaignImageET1 = findViewById(R.id.campaignImageET1);
        campaignGoalET = findViewById(R.id.campaignGoalET);
        campaignEndDateET = findViewById(R.id.campaignEndDateET);
        Button saveButton = findViewById(R.id.saveCampaign);

        setInitialDateTime();

        saveButton.setOnClickListener(this);

        categoryDisposable = Observable
                .fromCallable(new CallableCategoryGetRequest(this.getToken()))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<List<CampaignCategory>>) this::mapCategories);
        if (campaignId != 0) {
            campaignDisposable = Observable
                    .fromCallable(new CallableCampaignGetRequest(campaignId))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe((Consumer<Campaign>) this::mapCampaignValues);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        categoryRequestDispose();
        campaignRequestDispose();
    }

    private void mapCategories(List<CampaignCategory> categories) {
        List<String> categoryNames = new ArrayList<>();
        for (CampaignCategory category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditCampaignActivity.this,
                android.R.layout.simple_spinner_item, categoryNames);
        categorySpinner.setAdapter(adapter);

    }

    private void mapCampaignValues(Campaign campaign) {
        campaignTitleET.setText(campaign.getTitle());
        campaignDescriptionET.setText(campaign.getDescription());
        campaignImageET1.setText(campaign.getImages().get(0).getLink());
        campaignGoalET.setText(String.valueOf(campaign.getGoal()));
        campaignEndDateET.setText(String.valueOf(campaign.getExpiresAt()));
    }

    private Boolean isInputValid() {
        return true;
    }

    private void setInput(CreateCampaignRequest request) throws ParseException {
        List<String> images = new ArrayList<>();
        images.add(campaignImageET1.getText().toString());

        request.setTitle(campaignTitleET.getText().toString());
        request.setCategory((String) categorySpinner.getSelectedItem());
        request.setDescription(campaignDescriptionET.getText().toString());
        request.setImages(images);
        request.setGoal(Float.parseFloat(campaignGoalET.getText().toString()));
        request.setExpiresAt(new SimpleDateFormat("MMMM d,yyyy", Locale.getDefault())
                .parse(campaignEndDateET.getText().toString()).toInstant().toString());
        request.setVideoLink(DEFAULT_VIDEO);
    }

    private void redirectToCampaign() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void campaignRequestDispose() {
        if (campaignDisposable != null) {
            campaignDisposable.dispose();
        }
    }

    private void categoryRequestDispose() {
        if (categoryDisposable != null) {
            categoryDisposable.dispose();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.saveCampaign) {
            if (isInputValid()) {
                ApiRequests requests = RetrofitService.getInstance().getApiRequests();
                Call<Void> call;

                CreateCampaignRequest request = new CreateCampaignRequest();

                try {
                    setInput(request);
                } catch (ParseException e) {
                    Log.e(Utils.TAG, e.getMessage());
                }

                if (campaignId > 0) {
                    call = requests.updateCampaign(this.getToken(), campaignId, request);
                } else {
                    call = requests.createCampaign(this.getToken(), request);
                }

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            redirectToCampaign();
                        } else {
                            Utils.Toast(EditCampaignActivity.this, Utils.ERROR);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }

                });

            }
        }
    }

    public void setDate(View v) {
        new DatePickerDialog(EditCampaignActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateTime() {

        campaignEndDateET.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, monthOfYear);
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTime();
    };

}
