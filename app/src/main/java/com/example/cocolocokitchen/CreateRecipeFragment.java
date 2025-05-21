// Comments by ArcLKN
// Last modified by ArcLKN

package com.example.cocolocokitchen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateRecipeFragment extends Fragment {

    private KitchenDB kitchenDB;

    LinearLayout ingredientListContainer;
    List<Ingredient> ingredientList = new ArrayList<>();

    LinearLayout utensilListContainer;
    List<Utensil> utensilList = new ArrayList<>();

    LinearLayout stepListContainer;
    List<Step> stepList = new ArrayList<>();

    private Uri imageUri;
    private String costDegreeValue;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ImageView recipeImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_recipe, container, false);

        // Our database related class.
        kitchenDB = new KitchenDB(getContext());

        // Listener to create the recipe.
        Button createRecipeButton = view.findViewById(R.id.create_recipe_button);
        createRecipeButton.setOnClickListener(v -> createRecipe(view));

        // Define the price of the recipe when clicking on one of the following buttons.
        Button priceLowButton = view.findViewById(R.id.create_recipe_price_low);
        Button priceMediumButton = view.findViewById(R.id.create_recipe_price_medium);
        Button priceHighButton = view.findViewById(R.id.create_recipe_price_high);
        priceLowButton.setOnClickListener(v -> costDegreeValue = priceLowButton.getText().toString().trim());
        priceMediumButton.setOnClickListener(v -> costDegreeValue = priceMediumButton.getText().toString().trim());
        priceHighButton.setOnClickListener(v -> costDegreeValue = priceHighButton.getText().toString().trim());

        // Listeners to addThing buttons, each opening their own dialog box to enter details.
        ingredientListContainer = view.findViewById(R.id.create_recipe_ingredient_list_container);
        utensilListContainer = view.findViewById(R.id.create_recipe_utensil_list_container);
        stepListContainer = view.findViewById(R.id.create_recipe_step_list_container);

        Button addIngredientButton = view.findViewById(R.id.create_recipe_button_ingredient);
        addIngredientButton.setOnClickListener(v -> showAddIngredientDialog());

        Button addUtensilButton = view.findViewById(R.id.create_recipe_button_utensil);
        addUtensilButton.setOnClickListener(v -> showAddUtensilDialog());

        Button addStepButton = view.findViewById(R.id.create_recipe_button_step);
        addStepButton.setOnClickListener(v -> showAddStepDialog());

        // This make possible for the user to select an image from his gallery for the recipe.
        recipeImageView = view.findViewById(R.id.create_recipe_image);
        // Handles the result of launching another activity (here the image picker)
        pickImageLauncher = registerForActivityResult(
                // If a result is existing it assigns its data to imageUri which is used to set the image.
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        recipeImageView.setImageURI(imageUri); // Display selected image
                    }
                }
        );
        recipeImageView.setOnClickListener(v -> {
            // Creates an intent pointing to the user device's gallery storage.
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Limits the selection to image only.
            intent.setType("image/*");
            // Launch the image picker app.
            pickImageLauncher.launch(intent);
        });


        return view;
    }

    // When the createRecipe button is pressed, fetch every value from the different fields and
    // create a new recipe with them.
    // It then adds it to the recipesList through the SharedViewModel and to the database through KitchenDB.
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

        String imageUriString = imageUri != null ? imageUri.toString() : null;

        Recipe new_recipe = new Recipe(0, title, desc, people, time, costDegreeValue, imageUriString, R.drawable.recipe_default, ingredientList, utensilList, stepList, false, null, null, source);

        recipes.add(new_recipe);
        int newId = kitchenDB.insertRecipe(new_recipe);
        new_recipe.setId(newId);


        Toast.makeText(getContext(), "New Recipe Created!", Toast.LENGTH_SHORT).show();
    }

    // Dialog box showing when clicking on 'Add Ingredient' button.
    // The dialog box has three fields: name, quantity and quantity type.
    // It then uses the entered values to add a new entry to an IngredientList container.
    private void showAddIngredientDialog() {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_ingredient, null);

        EditText ingredientNameInput = dialogView.findViewById(R.id.ingredient_name_input);
        EditText ingredientQuantityInput = dialogView.findViewById(R.id.ingredient_quantity_input);
        Spinner ingredientUnitSpinner = dialogView.findViewById(R.id.ingredient_unit_spinner);

        List<UnitItem> unitItems = Arrays.asList(
                new UnitItem("Common", true),
                new UnitItem("Quantity", false),
                new UnitItem("Pinch", false),
                new UnitItem("Drop", false),
                new UnitItem("Teaspoon", false),
                new UnitItem("Tablespoon", false),
                new UnitItem("Cup", false),

                new UnitItem("Weight", true),
                new UnitItem("Gramme", false),
                new UnitItem("kg", false),

                new UnitItem("Volume", true),
                new UnitItem("ml", false),
                new UnitItem("cl", false),
                new UnitItem("L", false),

                new UnitItem("Other", true),
                new UnitItem("Slice", false)
        );

        UnitSpinnerAdapter adapter = new UnitSpinnerAdapter(requireContext(), unitItems);
        ingredientUnitSpinner.setAdapter(adapter);


        new AlertDialog.Builder(requireContext())
                .setTitle("Add Ingredient")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = ingredientNameInput.getText().toString().trim();
                    String quantity = ingredientQuantityInput.getText().toString().trim();
                    UnitItem selectedItem = (UnitItem) ingredientUnitSpinner.getSelectedItem();
                    String quantityType = "Quantity";
                    if (selectedItem != null && !selectedItem.isHeader) {
                        quantityType = selectedItem.name;
                    }

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
                                1.0f
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

                        ingredientList.add(new Ingredient(name, quantity, quantityType));

                        Toast.makeText(getContext(), "Added: " + name + " - " + quantity, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Works the same as the Ingredient Dialog Box.
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
                                1.0f
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

    // Works the same as the Ingredient Dialog Box.
    private void showAddStepDialog() {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_step, null);

        EditText stepNameInput = dialogView.findViewById(R.id.step_title_input);
        EditText stepDescriptionInput = dialogView.findViewById(R.id.step_description_input);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add step")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = stepNameInput.getText().toString().trim();
                    String description = stepDescriptionInput.getText().toString().trim();

                    if (!title.isEmpty() && !description.isEmpty()) {

                        LinearLayout stepEntryLayout = new LinearLayout(requireContext());
                        stepEntryLayout.setOrientation(LinearLayout.VERTICAL);
                        stepEntryLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        stepEntryLayout.setPadding(16, 16, 16, 16);
                        stepEntryLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.custom_input));

                        LinearLayout titleRowLayout = new LinearLayout(requireContext());
                        titleRowLayout.setOrientation(LinearLayout.HORIZONTAL);
                        titleRowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));

                        TextView stepTitleView = new TextView(requireContext());
                        stepTitleView.setText(title);
                        stepTitleView.setTextSize(18);
                        stepTitleView.setLayoutParams(new LinearLayout.LayoutParams(
                                0,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                1.0f
                        ));
                        stepTitleView.setPadding(0, 0, 8, 0);

                        Button deleteButton = new Button(requireContext());
                        deleteButton.setText("X");
                        deleteButton.setContentDescription("Delete step");
                        deleteButton.setTextSize(12);
                        int widthInDp = 48;
                        float scale = requireContext().getResources().getDisplayMetrics().density;
                        int widthInPx = (int) (widthInDp * scale + 0.5f);
                        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(widthInPx, widthInPx));
                        deleteButton.setOnClickListener(v -> stepListContainer.removeView(stepEntryLayout));

                        titleRowLayout.addView(stepTitleView);
                        titleRowLayout.addView(deleteButton);

                        TextView stepDescriptionView = new TextView(requireContext());
                        stepDescriptionView.setText(description);
                        stepDescriptionView.setTextSize(14);
                        stepDescriptionView.setPadding(0, 8, 0, 0);

                        stepEntryLayout.addView(titleRowLayout);
                        stepEntryLayout.addView(stepDescriptionView);

                        stepListContainer.addView(stepEntryLayout);

                        Step newStep = new Step(title, description);
                        stepList.add(newStep);

                        Toast.makeText(getContext(), "Added: " + title, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Please fill in both fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}