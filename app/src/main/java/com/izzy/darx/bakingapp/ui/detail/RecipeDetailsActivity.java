package com.izzy.darx.bakingapp.ui.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.izzy.darx.bakingapp.R;
import com.izzy.darx.bakingapp.adapter.RecipeDetailsAdapter;
import com.izzy.darx.bakingapp.model.Ingredient;
import com.izzy.darx.bakingapp.model.Recipe;
import com.izzy.darx.bakingapp.model.Step;
import com.izzy.darx.bakingapp.ui.baking.BakingActivity;
import com.izzy.darx.bakingapp.ui.recipe.RecipeActivity;
import com.izzy.darx.bakingapp.utils.BakingConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static final String RECIPE_LIST_STATE = "recipe_details_list";
    public static final String RECIPE_JSON_STATE = "recipe_json_list";

    @BindView(R.id.rv_recipe_ingredients)
    RecyclerView mRecyclerView;

    @BindView(R.id.btn_start_cooking)
    Button mButtonStartCooking;

    RecipeDetailsAdapter mRecipeDetailsAdapter;
    ArrayList<Recipe> mRecipeArrayList;
    ArrayList<Step> mStepArrayList;
    String mJsonResult;
    List<Ingredient> mIngredientList;
    private boolean isTablet;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        // Up navigation
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        isTablet = findViewById(R.id.recipe_details) != null;
       /* checkOrientation();*/

        if (getIntent().getStringExtra(BakingConstants.WIDGET_EXTRA) != null) {
            SharedPreferences sharedPreferences = getSharedPreferences(BakingConstants.BAKING_SHARED_PREF, MODE_PRIVATE);
            String jsonRecipe = sharedPreferences.getString(BakingConstants.JSON_RESULT_EXTRAS, "");
            mJsonResult = jsonRecipe;

            Gson gson = new Gson();
            Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);

            mStepArrayList = (ArrayList<Step>) recipe.getSteps();
            mIngredientList = recipe.getIngredients();

        } else {
            if (savedInstanceState != null) {
                mRecipeArrayList = savedInstanceState.getParcelableArrayList(RECIPE_LIST_STATE);
                mJsonResult = savedInstanceState.getString(RECIPE_JSON_STATE);
                mStepArrayList = (ArrayList<Step>) mRecipeArrayList.get(0).getSteps();
                mIngredientList = mRecipeArrayList.get(0).getIngredients();
            }else {

                //get Recipe from intent extra
                Intent intent = getIntent();
                mRecipeArrayList = intent.getParcelableArrayListExtra(BakingConstants.RECIPE_INTENT_EXTRAS);
                mJsonResult = intent.getStringExtra(BakingConstants.JSON_RESULT_EXTRAS);
                SharedPreferences sharedPreferences = getSharedPreferences(BakingConstants.BAKING_SHARED_PREF, MODE_PRIVATE);
                String jsonRecipe = sharedPreferences.getString(BakingConstants.JSON_RESULT_EXTRAS, "");
                Gson gson = new Gson();
                mJsonResult = jsonRecipe;
                Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);
                mStepArrayList = (ArrayList<Step>) recipe.getSteps();
                mIngredientList = recipe.getIngredients();

            }
        }

        mRecipeDetailsAdapter =  new RecipeDetailsAdapter(mIngredientList, RecipeDetailsActivity.this);
        mRecyclerView.setAdapter(mRecipeDetailsAdapter);

        RecyclerView.LayoutManager mLayoutManager;
        if(isTablet){
            mLayoutManager = new GridLayoutManager(this, 2);
        }
        else{
            mLayoutManager = new LinearLayoutManager(this);
        }

        mRecyclerView.setLayoutManager(mLayoutManager);

        mButtonStartCooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeDetailsActivity.this, BakingActivity.class);
                intent.putParcelableArrayListExtra(BakingConstants.STEP_INTENT_EXTRAS, mStepArrayList);
                intent.putExtra(BakingConstants.JSON_RESULT_EXTRAS, mJsonResult);
                startActivity(intent);
            }
        });

    }



    /*private void checkOrientation() {
        RecyclerView.LayoutManager mLayoutManager;
        if (this.mRecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager (this, 2);
            mLayoutManager = gridLayoutManager;
            mRecyclerView.setLayoutManager(mLayoutManager);
        } else if (this.mRecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST_STATE, mRecipeArrayList);
        outState.putString(RECIPE_JSON_STATE, mJsonResult);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RecipeDetailsActivity.this, RecipeActivity.class);
        startActivity(intent);
        finish();

    }
}
