package com.example.cocolocokitchen;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;

import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private RecipeAdapter recipeAdapter;

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_recipes, container, false);

        //Assigner toolbar en haut
        Toolbar toolbar = view.findViewById(R.id.recipes_toolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        // Register the MenuProvider
        activity.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.recipes_menu, menu);

                MenuItem searchItem = menu.findItem(R.id.recipes_action_search);
                if (searchItem != null) {
                    SearchView searchView = (SearchView) searchItem.getActionView();
                    if (searchView != null) {
                        searchView.setQueryHint("Search recipes...");
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                recipeAdapter.filter(newText);
                                return true;
                            }
                        });
                    } else {
                        Log.e("RecipesFragment", "SearchView is null. Check if the menu item uses actionViewClass or app:actionViewClass.");
                    }
                } else {
                    Log.e("RecipesFragment", "MenuItem recipes_action_search not found.");
                }
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

        TextView BottomTypeMenuButton = view.findViewById(R.id.recipes_button_type_menu);

        BottomTypeMenuButton.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            View sheetView = getLayoutInflater().inflate(R.layout.recipes_bottom_type_menu, null);
            dialog.setContentView(sheetView);

            RadioButton option1 = sheetView.findViewById(R.id.radio_option1);
            RadioButton option4 = sheetView.findViewById(R.id.radio_option4);

            Drawable[] drawables = BottomTypeMenuButton.getCompoundDrawables();

            option1.setOnClickListener(lambda -> {
                recipeAdapter.filterFavorite(false);
                dialog.dismiss();
                BottomTypeMenuButton.setText("File");
                Drawable favoriteDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.icon_file);
                BottomTypeMenuButton.setCompoundDrawablesWithIntrinsicBounds(favoriteDrawable, null, drawables[2], null); // Remove drawable
            });

            option4.setOnClickListener(lambda -> {
                recipeAdapter.filterFavorite(true);
                dialog.dismiss();
                BottomTypeMenuButton.setText("Favorite");
                Drawable favoriteDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_border_24);
                BottomTypeMenuButton.setCompoundDrawablesWithIntrinsicBounds(favoriteDrawable, null, drawables[2], null); // Remove drawable

            });

            dialog.show();
        });

        //Get recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recipes_recycler_view);


        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Recipe> recipes = viewModel.getRecipeList();

        recipeAdapter = new RecipeAdapter(requireContext(), recipes);
        recipeAdapter.setOnRecipeClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(recipeAdapter);

        FloatingActionButton createRecipeFloatingButton = view.findViewById(R.id.recipe_floatingActionButton);
        createRecipeFloatingButton.setOnClickListener(v -> {
            Fragment createRecipeFragment = new CreateRecipeFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, createRecipeFragment) // Use your container ID here
                    .addToBackStack(null) // Optional: enables back navigation
                    .commit();
        });

        //Toast.makeText(getContext(), "Recipe list loaded", Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onRecipeClick(Recipe recipe) {

    }
}
