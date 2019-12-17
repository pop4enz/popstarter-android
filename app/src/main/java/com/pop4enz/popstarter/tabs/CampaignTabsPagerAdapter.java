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
import com.pop4enz.popstarter.model.Comment;
import com.pop4enz.popstarter.model.CommentList;

public class CampaignTabsPagerAdapter extends FragmentPagerAdapter {

    DescriptionFragment descriptionFragment;
    CommentsFragment commentsFragment;

    @StringRes
    private static final int[] TAB_TITLES =
            new int[] { R.string.DESCRIPTION_TEXT, R.string.UPDATES_TEXT, R.string.COMMENTS_TEXT};

    private final Context mContext;

    public CampaignTabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        descriptionFragment = DescriptionFragment.newInstance(null);
        commentsFragment = CommentsFragment.newInstance(null);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return descriptionFragment;
            case 1:
                return DescriptionFragment.newInstance("No updates yet :(");
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

    @Override
    public int getCount() {
        return 3;
    }
}
