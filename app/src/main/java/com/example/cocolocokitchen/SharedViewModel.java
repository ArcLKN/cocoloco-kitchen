package com.example.cocolocokitchen;

import android.app.Application;
import android.database.Cursor;

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
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_NAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_DESCRIPTION));
                    int servings = cursor.getInt(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_SERVINGS));
                    int prepTime = cursor.getInt(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_PREP_TIME));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_PRICE_LEVEL));
                    String source = cursor.getString(cursor.getColumnIndexOrThrow(KitchenDB.RECIPE_COLUMN_SOURCE));

                    Recipe recipe = new Recipe(
                            name,
                            description,
                            servings,
                            prepTime,
                            price,
                            source,
                            R.drawable.aymen_cinnamon_rolls, // Temporary image
                            null,
                            null,
                            null,
                            false,
                            null,
                            null
                    );

                    recipeList.add(recipe);
                } while (cursor.moveToNext());
                cursor.close();
            }
            if (recipeList.isEmpty()) {
                Recipe recipe1 = new Recipe("Cinnamon & Orange Flaky Rolls", "An orange CinnamonRoll",
                        4, 60, "$", null,
                        R.drawable.aymen_cinnamon_rolls, null,
                        null, null, false, null, null);
                for (int i = 0; i < 10; i++) {
                    recipeList.add(recipe1);
                }
            }
        }
        return recipeList;
    }
}