package com.izzy.darx.bakingapp.network;

import com.izzy.darx.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
