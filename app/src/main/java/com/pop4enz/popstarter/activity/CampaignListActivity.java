package com.pop4enz.popstarter.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.adapter.MiniCampaignAdapter;
import com.pop4enz.popstarter.model.MiniCampaign;
import com.pop4enz.popstarter.retrofit.CallableCampaignListRequest;
import com.pop4enz.popstarter.utils.Utils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CampaignListActivity extends NavigationActivity implements MiniCampaignAdapter.OnCampaignListener {

    private RecyclerView campaignListRv;
    private MiniCampaignAdapter miniCampaignAdapter =
            new MiniCampaignAdapter(this);
    private Disposable campaignRequestDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_campaign_list);
        super.onCreate(savedInstanceState);

        campaignListRv = findViewById(R.id.campaignListRv);
        campaignListRv.setLayoutManager(new LinearLayoutManager(this));
        campaignListRv.setAdapter(miniCampaignAdapter);

        Observable.fromCallable(new CallableCampaignListRequest())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        mapCampaigns((List<MiniCampaign>) o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.Toast(CampaignListActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        campaignRequestDispose();
    }

    private void campaignRequestDispose() {
        if (campaignRequestDisposable != null) {
            campaignRequestDisposable.dispose();
        }
    }

    private void mapCampaigns(List<MiniCampaign> campaigns) {
        miniCampaignAdapter.setItems(campaigns);
        campaignRequestDispose();
    }

    @Override
    public void onCampaignClick(int position) {
        Intent intent = new Intent(this, CampaignActivity.class);
        intent.putExtra("campaignId", miniCampaignAdapter.getItemId(position));
        startActivity(intent);
    }
}
