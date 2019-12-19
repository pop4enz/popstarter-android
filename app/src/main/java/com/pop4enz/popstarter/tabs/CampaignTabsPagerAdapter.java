package com.pop4enz.popstarter.tabs;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.fragments.CommentsFragment;
import com.pop4enz.popstarter.fragments.DescriptionFragment;
import com.pop4enz.popstarter.fragments.RewardsFragment;
import com.pop4enz.popstarter.model.Comment;
import com.pop4enz.popstarter.model.CommentList;
import com.pop4enz.popstarter.model.Reward;
import com.pop4enz.popstarter.model.RewardList;

public class CampaignTabsPagerAdapter extends FragmentStatePagerAdapter {

    DescriptionFragment descriptionFragment;
    CommentsFragment commentsFragment;
    RewardsFragment rewardsFragment;

    @StringRes
    private static final int[] TAB_TITLES =
            new int[] { R.string.DESCRIPTION_TAB, R.string.REWARDS_TAB, R.string.COMMENTS_TAB};

    private final Context mContext;

    public CampaignTabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        descriptionFragment = DescriptionFragment.newInstance(null);
        commentsFragment = CommentsFragment.newInstance(null);
        rewardsFragment = RewardsFragment.newInstance(null);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return descriptionFragment;
            case 1:
                return rewardsFragment;
            case 2:
                return commentsFragment;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }


    public void setDescription(String description) {
        descriptionFragment.setDescription(description);
    }

    public void setComments(CommentList comments) {
        commentsFragment.setComments(comments);
    }

    public void addComment(Comment comment) {
        commentsFragment.addComment(comment);
    }

    public void setRewards(RewardList rewards) {
        rewardsFragment.setRewards(rewards);
    }

    public void addReward(Reward reward) {
        rewardsFragment.addReward(reward);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
