package com.izzy.darx.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.izzy.darx.bakingapp.R;
import com.izzy.darx.bakingapp.ui.detail.RecipeDetailsActivity;
import com.izzy.darx.bakingapp.utils.BakingConstants;

public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget (Context context, String recipeIngredients, int imgId, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(BakingConstants.WIDGET_EXTRA, "FETCHED_FROM_WIDGET");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        if(recipeIngredients == null){
            recipeIngredients = "No ingredients available!";
        }

        views.setTextViewText(R.id.tv_ingredients, recipeIngredients);
        views.setImageViewResource(R.id.ivRecipeIcon, imgId);

        views.setOnClickPendingIntent(R.id.tv_ingredients, pendingIntent);

        appWidgetManager .updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BakingWidgetService.startActionOpenRecipe(context);
    }

    public static void updateWidgetRecipe(Context context, String jsonRecipe , int imgId, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds){
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, jsonRecipe, imgId, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }



}
