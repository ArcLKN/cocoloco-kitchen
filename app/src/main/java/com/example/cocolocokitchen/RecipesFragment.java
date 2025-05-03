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
import androidx.core.view.MenuProvider;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
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

        Toolbar toolbar = view.findViewById(R.id.recipes_toolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        // Register the MenuProvider
        activity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.recipes_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.recipes_action_search) {
                    // handle search
                    return true;
                } else if (id == R.id.recipes_group_display_small) {
                    recipeAdapter.setViewType(0);
                    return true;
                } else if (id == R.id.recipes_group_display_big) {
                    recipeAdapter.setViewType(-1);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED); // lifecycle-aware!

        LinearLayout BottomTypeMenuButton = view.findViewById(R.id.recipes_button_type_menu);

        BottomTypeMenuButton.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            View sheetView = getLayoutInflater().inflate(R.layout.recipes_bottom_type_menu, null);
            dialog.setContentView(sheetView);
            dialog.show();
        });

        RecyclerView recyclerView = view.findViewById(R.id.recipes_recycler_view);

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Recipe> recipes = viewModel.getRecipeList();

        recipeAdapter = new RecipeAdapter(requireContext(), recipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(recipeAdapter);

        //Toast.makeText(getContext(), "Recipe list loaded", Toast.LENGTH_SHORT).show();

        return view;
    }

}
