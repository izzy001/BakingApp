package com.izzy.darx.bakingapp.ui.recipe;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.izzy.darx.bakingapp.R;
import com.izzy.darx.bakingapp.adapter.RecipeAdapter;
import com.izzy.darx.bakingapp.model.Recipe;
import com.izzy.darx.bakingapp.network.ApiClient;
import com.izzy.darx.bakingapp.network.ApiInterface;
import com.izzy.darx.bakingapp.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity {
    private static final String LOG_TAG = RecipeActivity.class.getSimpleName();
    public static final String RECIPE_JSON_STATE = "recipe_json_state";
    public  static final String RECIPE_ARRAYLIST_STATE = "recipe_arraylist_state";

    private ApiInterface mApiInterface;
    private GridLayoutManager gridLayoutManager;
    RecipeAdapter recipeAdapter;
    String mJsonResult;
    ArrayList<Recipe> mRecipeArrayList = new ArrayList<>();

    @BindView( R.id.rv_recipes)
    RecyclerView mRecyclerViewRecipes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);



        ButterKnife.bind(this);
        checkOrientation();

        if (savedInstanceState != null) {
            mJsonResult = savedInstanceState.getString(RECIPE_JSON_STATE);
            mRecipeArrayList = savedInstanceState.getParcelableArrayList(RECIPE_ARRAYLIST_STATE);
            recipeAdapter = new RecipeAdapter(RecipeActivity.this, mRecipeArrayList, mJsonResult);
            checkOrientation();
            mRecyclerViewRecipes.setAdapter(recipeAdapter);
        } else {
            if (NetworkUtils.isConnected(this)) {
                mApiInterface = new ApiClient().mApiInterface;
                new FetchRecipeAsync().execute();
            } else {
                showErrorMessage();
            }
        }
    }


    private void checkOrientation() {
        RecyclerView.LayoutManager mLayoutManager;
        if (this.mRecyclerViewRecipes.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerViewRecipes.setLayoutManager(gridLayoutManager);
            } else if (this.mRecyclerViewRecipes.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
             mLayoutManager = new LinearLayoutManager(this);
             mRecyclerViewRecipes.setLayoutManager(mLayoutManager);

        }

    }

    public void showErrorMessage(){
        Toast.makeText(this, "Error loading recipe, Please check your network connection", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchRecipeAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            fetchRecipes();
            return null;
        }
    }


    //FetchRecipes
    private void fetchRecipes() {
        Call<ArrayList<Recipe>> call = mApiInterface.getRecipes();

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                mRecipeArrayList = response.body();
                mJsonResult = new Gson().toJson(response.body());
                recipeAdapter = new RecipeAdapter(RecipeActivity.this, mRecipeArrayList, mJsonResult);
                RecyclerView.LayoutManager mLayoutManager;
                checkOrientation();
                mRecyclerViewRecipes.setAdapter(recipeAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {

                Log.d(LOG_TAG, t.toString());

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RECIPE_JSON_STATE, mJsonResult);
        outState.putParcelableArrayList(RECIPE_ARRAYLIST_STATE, mRecipeArrayList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
