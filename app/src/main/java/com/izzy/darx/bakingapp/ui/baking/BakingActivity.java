package com.izzy.darx.bakingapp.ui.baking;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.izzy.darx.bakingapp.R;
import com.izzy.darx.bakingapp.adapter.StepListAdapter;
import com.izzy.darx.bakingapp.model.Step;
import com.izzy.darx.bakingapp.ui.VideoPlayerFragment;
import com.izzy.darx.bakingapp.ui.detail.RecipeDetailsActivity;
import com.izzy.darx.bakingapp.utils.BakingConstants;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingActivity extends AppCompatActivity implements View.OnClickListener, StepListAdapter.OnStepClick {

    public static final String STEP_LIST_STATE = "step_list_state";
    public static final String STEP_NUMBER_STATE = "step_number_state";
    public static final String STEP_LIST_JSON_STATE = "step_list_json_state";
    private int mVideoNumber = 0;

    @BindView(R.id.fl_player_container)
    FrameLayout mPlayerContainer;

    @BindView(R.id.btn_next_step)
    Button mBtnNextStep;

    @BindView(R.id.btn_previous_step)
    Button mBtnPreviousStep;

    @Nullable
    @BindView(R.id.rv_recipe_steps)
    RecyclerView mRecyclerViewSteps;

    ArrayList<Step> mStepArrayList = new ArrayList<>();
    String mJsonResult;
    boolean isFromWidget;
    StepListAdapter mStepListAdapter;
    private boolean isTablet;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking);

        //Up Navigation
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        isTablet = findViewById(R.id.baking) != null;



        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(BakingConstants.STEP_INTENT_EXTRAS)) {
                mStepArrayList = getIntent().getParcelableArrayListExtra(BakingConstants.STEP_INTENT_EXTRAS);
            }
            if (intent.hasExtra(BakingConstants.JSON_RESULT_EXTRAS)) {
                mJsonResult = getIntent().getStringExtra(BakingConstants.JSON_RESULT_EXTRAS);
            }
            isFromWidget = intent.getStringExtra(BakingConstants.WIDGET_EXTRA) != null;
        }

        //if there is  no saved state, instantiate fragment
        if (savedInstanceState == null) {
            playVideo(mStepArrayList.get(mVideoNumber));
        }

        ButterKnife.bind(this);
        /*checkOrientation();*/
       handleUiForDevice();
    }

   /* void checkOrientation() {
        assert this.mRecyclerViewSteps != null;
        if (this.mRecyclerViewSteps.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mBtnPreviousStep.setOnClickListener(this);
            mBtnNextStep.setOnClickListener(this);
        } else if (this.mRecyclerViewSteps.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mStepListAdapter = new StepListAdapter(this, mStepArrayList, this, mVideoNumber);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerViewSteps.setLayoutManager(mLinearLayoutManager);
            mRecyclerViewSteps.setAdapter(mStepListAdapter);
           *//* mBtnPreviousStep.setVisibility(View.INVISIBLE);
            mBtnNextStep.setVisibility(View.INVISIBLE);*//*
        }

    }*/

    void playVideo(Step step){
        //TODO initialize video player fragment later
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle stepsBundle =  new Bundle();
        stepsBundle.putParcelable(BakingConstants.STEP_SINGLE, step);
        videoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fl_player_container, videoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    public void playVideoReplace(Step step){
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable(BakingConstants.STEP_SINGLE, step);
        videoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_player_container, videoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST_STATE, mStepArrayList);
        outState.putString(STEP_LIST_JSON_STATE, mJsonResult);
        outState.putInt(STEP_NUMBER_STATE, mVideoNumber);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        //If it's last step show baking is over
        if(mVideoNumber == mStepArrayList.size()-1){
            Toast.makeText(this, R.string.baking_is_over, Toast.LENGTH_SHORT).show();
        }
        else{
            if(v.getId() == mBtnPreviousStep.getId()){
                mVideoNumber--;
                if(mVideoNumber < 0){
                    Toast.makeText(this, R.string.see_next_step, Toast.LENGTH_SHORT).show();
                }
                else
                    playVideoReplace(mStepArrayList.get(mVideoNumber));
            }
            else if(v.getId() == mBtnNextStep.getId()){
                mVideoNumber++;
                playVideoReplace(mStepArrayList.get(mVideoNumber));
            }
        }


    }

    @Override
    public void onStepClick(int position) {

        mVideoNumber = position;
        playVideoReplace(mStepArrayList.get(position));

    }

    public void handleUiForDevice() {
        if(!isTablet){
            // Set button listeners
            mBtnNextStep.setOnClickListener(this);
            mBtnPreviousStep.setOnClickListener(this);
        }
        else{//Tablet view
            mStepListAdapter = new StepListAdapter(this, mStepArrayList, this, mVideoNumber);
            mLinearLayoutManager = new LinearLayoutManager(this);
            assert mRecyclerViewSteps != null;
            mRecyclerViewSteps.setLayoutManager(mLinearLayoutManager);
            mRecyclerViewSteps.setAdapter(mStepListAdapter);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStepArrayList = savedInstanceState.getParcelableArrayList(STEP_LIST_STATE);
            mJsonResult = savedInstanceState.getString(STEP_LIST_JSON_STATE);
            mVideoNumber = savedInstanceState.getInt(STEP_NUMBER_STATE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BakingActivity.this, RecipeDetailsActivity.class);
        startActivity(intent);
        finish();
    }
}
