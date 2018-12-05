package com.izzy.darx.bakingapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.izzy.darx.bakingapp.model.Step;
import com.izzy.darx.bakingapp.ui.VideoPlayerFragment;

import java.util.ArrayList;

public class StepPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Step> mStepList;
    private Bundle stepsBundle = new Bundle();

    public StepPagerAdapter(FragmentManager fm, ArrayList<Step> stepList) {
        super(fm);
        this.mStepList = stepList;
    }

    @Override
    public Fragment getItem(int position) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        stepsBundle.putParcelableArrayList("steps", mStepList);
        stepsBundle.putInt("page",position+1);
        stepsBundle.putBoolean("isLastPage",position == getCount() - 1);
        videoPlayerFragment.setArguments(stepsBundle);
        return videoPlayerFragment;
    }

    @Override
    public int getCount() {
        return mStepList.size();
    }
}
