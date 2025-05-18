package com.example.cocolocokitchen;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class SharedViewModel extends AndroidViewModel {

    private final List<Recipe> recipeList = new ArrayList<>();
    private final KitchenDB kitchenDB;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        kitchenDB = new KitchenDB(application.getApplicationContext());
    }

    public List<Recipe> getRecipeList() {
        if (recipeList.isEmpty()) {
            Cursor cursor = kitchenDB.getAllRecipes();
            Log.d("SharedViewModel", "Fetching recipes from database");
            Log.d("SharedViewModel", "Cursor count: " + cursor.getCount());

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_NAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_DESCRIPTION));
                    int servings = cursor.getInt(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_SERVINGS));
                    int prepTime = cursor.getInt(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_PREP_TIME));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_PRICE_LEVEL));
                    String source = cursor.getString(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_SOURCE));
                    String image_uri = cursor.getString(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_IMAGE_URI));
                    // Log.d("SharedViewModel", "Serving value: " + servings);

                    List<Ingredient> ingredients = kitchenDB.getIngredientsForRecipe(recipeId);
                    List<Utensil> utensils = kitchenDB.getUtensilsForRecipe(recipeId);
                    List<Step> steps = kitchenDB.getStepsForRecipe(recipeId);
                    boolean isFavorite = kitchenDB.isFavorite(recipeId);
                    //List<String> imagePaths = kitchenDB.getImagePathsForRecipe(recipeId);


                    Recipe recipe = new Recipe(
                            recipeId,
                            name,
                            description,
                            servings,
                            prepTime,
                            price,
                            image_uri,
                            R.drawable.recipe_default, // Temporary image
                            ingredients,
                            utensils,
                            steps,
                            isFavorite,
                            null,
                            null,
                            source
                    );

                    recipeList.add(recipe);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return recipeList;
    }

    public List<Recipe> resetRecipeList() {
        recipeList.clear();
        return getRecipeList();
    }

}