package com.example.cocolocokitchen;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CreateRecipeFragment extends Fragment {

    LinearLayout ingredientListContainer;
    List<Ingredient> ingredientList;

    LinearLayout utensilListContainer;
    List<Utensil> utensilList;

    LinearLayout stepListContainer;
    List<Step> stepList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_recipe, container, false);

        Button createRecipeButton = view.findViewById(R.id.create_recipe_button);
        createRecipeButton.setOnClickListener(v -> {
            createRecipe(view);
        });

        ingredientListContainer = view.findViewById(R.id.create_recipe_ingredient_list_container);
        utensilListContainer = view.findViewById(R.id.create_recipe_utensil_list_container);
        stepListContainer = view.findViewById(R.id.create_recipe_step_list_container);

        Button addIngredientButton = view.findViewById(R.id.create_recipe_button_ingredient);
        addIngredientButton.setOnClickListener(v -> showAddIngredientDialog());

        Button addUtensilButton = view.findViewById(R.id.create_recipe_button_utensil);
        addUtensilButton.setOnClickListener(v -> showAddUtensilDialog());

        Button addStepButton = view.findViewById(R.id.create_recipe_button_step);
        //addStepButton.setOnClickListener(v -> showAddStepDialog());

        return view;
    }

    public void createRecipe(View view) {

        EditText edit_text_title = view.findViewById(R.id.create_recipe_title);
        String title = "New Recipe";
        if (!edit_text_title.getText().toString().trim().isEmpty()) {
            title = edit_text_title.getText().toString().trim();
        }

        EditText edit_text_desc = view.findViewById(R.id.create_recipe_desc);
        String desc = edit_text_desc.getText().toString().trim();

        EditText edit_text_hours = view.findViewById(R.id.create_recipe_hours);
        int hours = 0;
        try {
            hours = Integer.parseInt(edit_text_hours.getText().toString().trim());
        } catch (NumberFormatException e) {
        }

        EditText edit_text_minutes = view.findViewById(R.id.create_recipe_minutes);
        int minutes = 0;
        try {
            minutes = Integer.parseInt(edit_text_minutes.getText().toString().trim());
        } catch (NumberFormatException e) {
        }

        int time = hours * 60 + minutes;

        EditText edit_text_people = view.findViewById(R.id.create_recipe_people);
        int people = 0;
        try {
            people = Integer.parseInt(edit_text_people.getText().toString().trim());
        } catch (NumberFormatException e) {
        }

        EditText edit_text_source = view.findViewById(R.id.create_recipe_source);
        String source = edit_text_source.getText().toString().trim();

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Recipe> recipes = viewModel.getRecipeList();

        Recipe new_recipe = new Recipe(title, desc, people, time, "$", null, R.drawable.recipe_default, ingredientList, utensilList, false, null, null);

        recipes.add(new_recipe);


        Toast.makeText(getContext(), "New Recipe Created!", Toast.LENGTH_SHORT).show();
    }

    private void showAddIngredientDialog() {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ingredient, null);

        EditText ingredientNameInput = dialogView.findViewById(R.id.ingredient_name_input);
        EditText ingredientQuantityInput = dialogView.findViewById(R.id.ingredient_quantity_input);
        Spinner ingredientUnitSpinner = dialogView.findViewById(R.id.ingredient_unit_spinner);

        String[] units = {"gramme", "kg", "ml", "L", "Cup", "Tablespoon", "Spoon"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientUnitSpinner.setAdapter(adapter);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Ingredient")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = ingredientNameInput.getText().toString().trim();
                    String quantity = ingredientQuantityInput.getText().toString().trim();
                    String quantityType = ingredientUnitSpinner.getSelectedItem().toString();

                    if (!name.isEmpty() && !quantity.isEmpty() && !quantityType.isEmpty()) {

                        LinearLayout ingredientEntryLayout = new LinearLayout(requireContext());
                        ingredientEntryLayout.setOrientation(LinearLayout.HORIZONTAL);
                        ingredientEntryLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        ingredientEntryLayout.setPadding(0, 8, 0, 8);


                        ingredientEntryLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.custom_input));


                        TextView ingredientView = new TextView(requireContext());
                        ingredientView.setText(quantity + " " + quantityType + " of " + name);
                        ingredientView.setTextSize(14);
                        ingredientView.setLayoutParams(new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f // Weight to take up available space
                        ));
                        ingredientView.setPadding(16, 8, 8, 8);


                        Button deleteButton = new Button(requireContext());
                        deleteButton.setText("X");
                        deleteButton.setTextSize(12);

                        int widthInDp = 48;
                        float scale = requireContext().getResources().getDisplayMetrics().density;
                        int widthInPx = (int) (widthInDp * scale + 0.5f);
                        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                                widthInPx,
                                widthInPx
                        ));


                        deleteButton.setOnClickListener(v -> ingredientListContainer.removeView(ingredientEntryLayout));

                        ingredientEntryLayout.addView(ingredientView);
                        ingredientEntryLayout.addView(deleteButton);

                        ingredientListContainer.addView(ingredientEntryLayout);

                        ingredientList.add(new Ingredient(name, Integer.parseInt(quantity), quantityType));

                        Toast.makeText(getContext(), "Added: " + name + " - " + quantity, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showAddUtensilDialog() {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_utensil, null);

        EditText utensilNameInput = dialogView.findViewById(R.id.utensil_name_input);
        EditText utensilQuantityInput = dialogView.findViewById(R.id.utensil_quantity_input);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Utensil")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = utensilNameInput.getText().toString().trim();
                    String quantity = utensilQuantityInput.getText().toString().trim();

                    if (!name.isEmpty() && !quantity.isEmpty()) {

                        LinearLayout utensilEntryLayout = new LinearLayout(requireContext());
                        utensilEntryLayout.setOrientation(LinearLayout.HORIZONTAL);
                        utensilEntryLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        utensilEntryLayout.setPadding(0, 8, 0, 8);


                        utensilEntryLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.custom_input));


                        TextView utensilView = new TextView(requireContext());
                        utensilView.setText(quantity + " " + name);
                        utensilView.setTextSize(14);
                        utensilView.setLayoutParams(new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f // Weight to take up available space
                        ));
                        utensilView.setPadding(16, 8, 8, 8);


                        Button deleteButton = new Button(requireContext());
                        deleteButton.setText("X");
                        deleteButton.setTextSize(12);

                        int widthInDp = 48;
                        float scale = requireContext().getResources().getDisplayMetrics().density;
                        int widthInPx = (int) (widthInDp * scale + 0.5f);
                        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                                widthInPx,
                                widthInPx
                        ));


                        deleteButton.setOnClickListener(v -> utensilListContainer.removeView(utensilEntryLayout));

                        utensilEntryLayout.addView(utensilView);
                        utensilEntryLayout.addView(deleteButton);

                        utensilListContainer.addView(utensilEntryLayout);

                        utensilList.add(new Utensil(name, Integer.parseInt(quantity)));

                        Toast.makeText(getContext(), "Added: " + name + " - " + quantity, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}