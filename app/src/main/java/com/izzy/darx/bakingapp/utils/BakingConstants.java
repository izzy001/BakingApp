package com.izzy.darx.bakingapp.utils;

import com.izzy.darx.bakingapp.R;

public class BakingConstants {

    public static final String JSON_RESULT_EXTRAS = "json_result_extras";
    public static final String WIDGET_EXTRA = "widget_extra";
    public static final String RECIPE_INTENT_EXTRAS = "recipe_intent_extras";
    public static final String STEP_SINGLE = "step_single";
    public static final String STEP_INTENT_EXTRAS = "step_intent_extra";
    public static final String BAKING_SHARED_PREF = "baking_shared_pref";

    public static String [] units = {"CUP", "TBLSP", "TSP", "G", "K", "OZ", "UNIT"};
    public static String [] unitName =  {"Cup", "Tablespoon", "Teaspoon", "Gram", "Kilogram", "Ounce", "Unit" };

    //unitIcons
    public static int[] unitIcons = {
            R.drawable.cup,
            R.drawable.tablespoon,
            R.drawable.teaspoon,
            R.drawable.grams,
            R.drawable.kilograms,
            R.drawable.ounce,
            R.drawable.unit
    };

    public static int[] recipeIcons = {
            R.drawable.brownie,
            R.drawable.nutella_pie,
            R.drawable.yellow_cake,
            R.drawable.cheesecake
    };

    //RecipeIcons will go in here

}
