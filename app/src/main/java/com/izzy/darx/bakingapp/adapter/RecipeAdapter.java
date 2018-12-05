package com.izzy.darx.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.izzy.darx.bakingapp.R;
import com.izzy.darx.bakingapp.model.Recipe;
import com.izzy.darx.bakingapp.ui.detail.RecipeDetailsActivity;
import com.izzy.darx.bakingapp.utils.BakingConstants;
import com.izzy.darx.bakingapp.widget.BakingWidgetService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final ArrayList<Recipe> mRecipeList;
    private final Context mContext;
    private String mJsonResult;
    private String recipeJson;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipeArrayList, String jsonResult) {
        mRecipeList = recipeArrayList;
        mContext = context;
        mJsonResult = jsonResult;
    }

    @NonNull
    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeAdapter.RecipeViewHolder holder, int position) {

        assert holder.recipeName != null;
        holder.recipeName.setText(mRecipeList.get(position).getName());

        switch (position) {
            case  0 :
                assert holder.recipeIcon != null;
                holder.recipeIcon.setImageResource(R.drawable.nutella_pie);
            break;

            case 1 :
                assert holder.recipeIcon != null;
                holder.recipeIcon.setImageResource(R.drawable.brownie);
            break;

            case 2 :
                assert holder.recipeIcon != null;
                holder.recipeIcon.setImageResource(R.drawable.yellow_cake);
            break;

            case 3 :
                assert holder.recipeIcon != null;
                holder.recipeIcon.setImageResource(R.drawable.cheesecake);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = mRecipeList.get(holder.getAdapterPosition());
                recipeJson = jsonToString(mJsonResult, holder.getAdapterPosition());
                Intent intent = new Intent(mContext, RecipeDetailsActivity.class);
                ArrayList<Recipe> recipeArrayList = new ArrayList<>();
                recipeArrayList.add(recipe);
                intent.putParcelableArrayListExtra(BakingConstants.RECIPE_INTENT_EXTRAS, recipeArrayList);
                intent.putExtra(BakingConstants.JSON_RESULT_EXTRAS, recipeJson);
                mContext.startActivity(intent);

                SharedPreferences.Editor editor = mContext.getSharedPreferences(BakingConstants.BAKING_SHARED_PREF, Context.MODE_PRIVATE).edit();
                editor.putString(BakingConstants.JSON_RESULT_EXTRAS, recipeJson);
                editor.apply();

                if(Build.VERSION.SDK_INT > 25){
                    //Start the widget service to update the widget
                    BakingWidgetService.setActionOpenRecipeO(mContext);
                }
                else{
                    //Start the widget service to update the widget
                    BakingWidgetService.startActionOpenRecipe(mContext);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_recipe_name)
        TextView recipeName;

        @Nullable
        @BindView(R.id.iv_recipe_icon)
        ImageView recipeIcon;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String jsonToString(String jsonResult, int position) {
        JsonElement jsonElement = new JsonParser().parse(jsonResult);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonElement recipeElement = jsonArray.get(position);
        return recipeElement.toString();
    }
}
