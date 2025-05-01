package com.example.cocolocokitchen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends AppCompatActivity {

    RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes);

         // TOOLBAR

        Toolbar toolbar = findViewById(R.id.recipes_toolbar);
        setSupportActionBar(toolbar);

        // SOMETHING ELSE

        LinearLayout BottomTypeMenuButton = findViewById(R.id.recipes_button_type_menu);

        BottomTypeMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog(RecipesActivity.this);
                View view = getLayoutInflater().inflate(R.layout.recipes_bottom_type_menu, null);
                dialog.setContentView(view);

                dialog.show();
            }
        });


        // RECYCLER VIEW
        RecyclerView recyclerView = findViewById(R.id.recipes_recycler_view);


        recipeAdapter = new RecipeAdapter(this, getRecipeList());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recipeAdapter);

        Toast.makeText(this, "Recipe list loaded", Toast.LENGTH_SHORT).show();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Get recipes from database.
    private List<Recipe> getRecipeList() {
        List<Recipe> recipeList = new ArrayList<>();

        Recipe recipe1 = new Recipe("Cinnamon & Orange Flaky Rolls", "An orange CinnamonRoll",
                4, 60, "$", null,
                R.drawable.aymen_cinnamon_rolls, null,
                null, false, null, null);

        recipeList.add(recipe1);
        recipeList.add(recipe1);
        recipeList.add(recipe1);
        recipeList.add(recipe1);
        recipeList.add(recipe1);
        recipeList.add(recipe1);
        recipeList.add(recipe1);
        recipeList.add(recipe1);
        recipeList.add(recipe1);
        recipeList.add(recipe1);
        recipeList.add(recipe1);
        return recipeList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.recipes_group_item_display) {
            return true;

        } else if (itemId == R.id.recipes_action_search) {
            // Handle search
            return true;

        } else if (itemId == R.id.recipes_group_display_small) {
            recipeAdapter.setViewType(0);
            return true;

        } else if (itemId == R.id.recipes_group_display_big) {
            recipeAdapter.setViewType(-1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}