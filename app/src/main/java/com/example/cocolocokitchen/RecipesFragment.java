package com.example.cocolocokitchen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {

    private RecipeAdapter recipeAdapter;

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_recipes, container, false);

        // Toolbar logic can be skipped or adjusted based on your layout
        // For bottom nav, you usually don't show a toolbar per fragment

        LinearLayout BottomTypeMenuButton = view.findViewById(R.id.recipes_button_type_menu);

        BottomTypeMenuButton.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            View sheetView = getLayoutInflater().inflate(R.layout.recipes_bottom_type_menu, null);
            dialog.setContentView(sheetView);
            dialog.show();
        });

        RecyclerView recyclerView = view.findViewById(R.id.recipes_recycler_view);

        recipeAdapter = new RecipeAdapter(requireContext(), getRecipeList());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(recipeAdapter);

        Toast.makeText(getContext(), "Recipe list loaded", Toast.LENGTH_SHORT).show();

        return view;
    }

    private List<Recipe> getRecipeList() {
        List<Recipe> recipeList = new ArrayList<>();

        Recipe recipe1 = new Recipe("Cinnamon & Orange Flaky Rolls", "An orange CinnamonRoll",
                4, 60, "$", null,
                R.drawable.aymen_cinnamon_rolls, null,
                null, false, null, null);

        for (int i = 0; i < 10; i++) {
            recipeList.add(recipe1);
        }

        return recipeList;
    }
}
