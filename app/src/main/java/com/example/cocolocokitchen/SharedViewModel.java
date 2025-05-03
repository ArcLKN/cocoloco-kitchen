package com.example.cocolocokitchen;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {

    private final List<Recipe> recipeList = new ArrayList<>();

    public List<Recipe> getRecipeList() {
        if (recipeList.isEmpty()) {
            Recipe recipe1 = new Recipe("Cinnamon & Orange Flaky Rolls", "An orange CinnamonRoll",
                    4, 60, "$", null,
                    R.drawable.aymen_cinnamon_rolls, null,
                    null, false, null, null);
            for (int i = 0; i < 10; i++) {
                recipeList.add(recipe1);
            }
        }
        return recipeList;
    }

    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
    }
}