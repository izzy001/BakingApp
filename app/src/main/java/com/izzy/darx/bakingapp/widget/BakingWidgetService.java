package com.izzy.darx.bakingapp.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.izzy.darx.bakingapp.model.Ingredient;
import com.izzy.darx.bakingapp.model.Recipe;
import com.izzy.darx.bakingapp.utils.BakingConstants;

import java.util.List;
import java.util.Objects;

public class BakingWidgetService extends IntentService {
    public static final String ACTION_OPEN_RECIPE = "com.izzy.darx.bakingapp.widget.baking_widget_service";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BakingWidgetService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my channel_01";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Baking Service", NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) Objects.requireNonNull(getSystemService(NOTIFICATION_SERVICE))).createNotificationChannel(notificationChannel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("")
                    .build();

            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_OPEN_RECIPE.equals(action)) {
                handleActionOpenRecipe();
            }

        }

    }


    private void handleActionOpenRecipe() {
        SharedPreferences sharedPreferences = getSharedPreferences(BakingConstants.BAKING_SHARED_PREF, MODE_PRIVATE);
        String jsonRecipe = sharedPreferences.getString(BakingConstants.JSON_RESULT_EXTRAS, "");
        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);
        int id = recipe.getId();
        int imgId = BakingConstants.recipeIcons[id-1];
        List<Ingredient> ingredientList = recipe.getIngredients();
        for (Ingredient ingredient : ingredientList) {
            String quantity = String.valueOf(ingredient.getQuantity());
            String measure = ingredient.getMeasure();
            String ingredientName = ingredient.getIngredient();
            String line = quantity + " " + measure + " " + ingredientName;
            stringBuilder.append(line).append("\n");
        }

        String ingredientsString = stringBuilder.toString();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));
        BakingWidgetProvider.updateWidgetRecipe(this, ingredientsString, imgId, appWidgetManager, appWidgetIds);
    }


    //trigger the service to perform action
    public static void startActionOpenRecipe(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        context.startService(intent);
    }

    //For Android O and above
    public static void setActionOpenRecipeO(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        ContextCompat.startForegroundService(context, intent);
    }
}
