package com.example.cocolocokitchen;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipesFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private RecipeAdapter recipeAdapter;
    RecipesGroupSectionAdapter groupAdapter; // adapter for grouped mode
    boolean isGroupedMode = false;
    private List<Recipe> recipes = new ArrayList<>();

    public RecipesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_recipes, container, false);

        //Assigner toolbar en haut
        Toolbar toolbar = view.findViewById(R.id.recipes_toolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

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
            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
            View sheetView = getLayoutInflater().inflate(R.layout.recipes_bottom_type_menu, null);
            dialog.setContentView(sheetView);

            RadioButton option1 = sheetView.findViewById(R.id.radio_option1);
            RadioButton option2 = sheetView.findViewById(R.id.radio_option2);
            RadioButton option4 = sheetView.findViewById(R.id.radio_option4);

            Drawable[] drawables = BottomTypeMenuButton.getCompoundDrawables();

            option1.setOnClickListener(lambda -> {
                recipeAdapter.filterFavorite(false);
                dialog.dismiss();
                switchToListMode();
                BottomTypeMenuButton.setText(R.string.RecipesModeList);
                Drawable favoriteDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_view_list_32);
                BottomTypeMenuButton.setCompoundDrawablesWithIntrinsicBounds(favoriteDrawable, null, drawables[2], null); // Remove drawable
            });

            option2.setOnClickListener(lambda -> {
                switchToGroupMode();
                dialog.dismiss();
                BottomTypeMenuButton.setText(R.string.RecipesModeGroup);
                Drawable viewDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_folder_open_32);
                BottomTypeMenuButton.setCompoundDrawablesWithIntrinsicBounds(viewDrawable, null, drawables[2], null); // Remove drawable

            });

            option4.setOnClickListener(lambda -> {
                recipeAdapter.filterFavorite(true);
                dialog.dismiss();
                switchToListMode();
                BottomTypeMenuButton.setText(R.string.RecipesModeFav);
                Drawable favoriteDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_border_32);
                BottomTypeMenuButton.setCompoundDrawablesWithIntrinsicBounds(favoriteDrawable, null, drawables[2], null); // Remove drawable

            });

            dialog.show();
        });

        RecyclerView recyclerView = view.findViewById(R.id.recipes_recycler_view);


        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        recipes = viewModel.getRecipeList();

        recipeAdapter = new RecipeAdapter(requireContext(), recipes);
        recipeAdapter.setOnRecipeClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(recipeAdapter);

        FloatingActionButton createRecipeFloatingButton = view.findViewById(R.id.recipe_floatingActionButton);
        createRecipeFloatingButton.setOnClickListener(v -> {
            Fragment createRecipeFragment = new CreateRecipeFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, createRecipeFragment)
                    .addToBackStack(null) // Optional: enables back navigation
                    .commit();
        });

        //Toast.makeText(getContext(), "Recipe list loaded", Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onRecipeClick(Recipe recipe) {

    }

    private void switchToListMode() {
        if (isGroupedMode) {
            RecyclerView recyclerView = requireView().findViewById(R.id.recipes_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(recipeAdapter);
            isGroupedMode = false;
        }
    }

    private void switchToGroupMode() {
        if (!isGroupedMode) {
            RecyclerView recyclerView = requireView().findViewById(R.id.recipes_recycler_view);

            String[] groupArray = getResources().getStringArray(R.array.recipe_group_array);
            Map<String, List<Recipe>> grouped = new HashMap<>();
            for (Recipe recipe : recipes) {
                String group = recipe.getGroup();
                if (group == null || group.isEmpty()) group = "None";
                grouped.computeIfAbsent(group, k -> new ArrayList<>()).add(recipe);
            }
            List<RecipesGroupSection> sections = new ArrayList<>();
            for (String groupName : groupArray) {
                List<Recipe> groupRecipes = grouped.get(groupName);
                if (groupRecipes != null && !groupRecipes.isEmpty()) {
                    sections.add(new RecipesGroupSection(groupName, "#FFFFFF", groupRecipes, false));
                }
            }

            groupAdapter = new RecipesGroupSectionAdapter(requireContext(), sections);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setAdapter(groupAdapter);

            isGroupedMode = true;
        }
    }

}
