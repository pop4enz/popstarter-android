package com.pop4enz.popstarter.tabs;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.fragments.CommentsFragment;
import com.pop4enz.popstarter.fragments.DescriptionFragment;
import com.pop4enz.popstarter.model.CommentList;

public class CampaignTabsPagerAdapter extends FragmentPagerAdapter {

    private String description;
    private CommentList comments;

    DescriptionFragment descriptionFragment;

    @StringRes
    private static final int[] TAB_TITLES =
            new int[] { R.string.descriptionText, R.string.updatesText, R.string.commentsText };

    private final Context mContext;

    public CampaignTabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        descriptionFragment = DescriptionFragment.newInstance(description);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return descriptionFragment;
            case 1:
                return DescriptionFragment.newInstance("No updates yet :(");
            case 2:
                return CommentsFragment.newInstance(comments);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        descriptionFragment.setDescription(description);
    }

    public CommentList getComments() {
        return comments;
    }

    public void setComments(CommentList comments) {
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
