package com.example.cocolocokitchen;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RecipeViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Recipe recipe;
    private KitchenDB kitchenDB;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeViewFragment newInstance(String param1, String param2) {
        RecipeViewFragment fragment = new RecipeViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_recipe_view, container, false);

        // Get the index from arguments
        if (getArguments() != null) {
            int index = getArguments().getInt("recipeIndex", -1);
            if (index != -1) {
                SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                recipe = viewModel.getRecipeList().get(index);

                // TODO: Now bind `recipe` to your views here
                // For example:
                TextView titleText = view.findViewById(R.id.recipe_name);
                titleText.setText(recipe.getTitle());

                TextView sourceText = view.findViewById(R.id.recipe_source);
                sourceText.setText("Author: " + recipe.getSource());

                TextView timeText = view.findViewById(R.id.recipe_time);
                timeText.setText(""+ recipe.getTimeInMinute() + " min");

                TextView groupText = view.findViewById(R.id.recipe_group);
                groupText.setText("" + recipe.getNumberOfServing());

                TextView costText = view.findViewById(R.id.recipe_cost);
                costText.setText(recipe.getCostDegree());

                ImageView imageView = view.findViewById(R.id.recipe_image_fragment);
                String imageUrl = recipe.getImageUrl();
                int imageResId = recipe.getImageResId();

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    // Load from URL
                    Glide.with(requireContext())
                            .load(imageUrl)
                            .into(imageView);
                } else {
                    // Load from drawable resource
                    Glide.with(requireContext())
                            .load(imageResId)
                            .into(imageView);
                }

                kitchenDB = new KitchenDB(getContext());

                ImageView favView = view.findViewById(R.id.recipe_do_favorite);
                updateFavoriteIcon(favView, recipe.isFavorite());
                favView.setOnClickListener(v -> {
                    boolean newFavoriteStatus = !recipe.isFavorite();
                    recipe.setFavorite(newFavoriteStatus); // update the recipe object
                    updateFavoriteIcon(favView, newFavoriteStatus);

                    kitchenDB.setIsFavorite(recipe.getId(), newFavoriteStatus);
                });

                ImageView deleteView = view.findViewById(R.id.recipe_delete);
                deleteView.setOnClickListener(v -> {
                    kitchenDB.deleteRecipe(recipe.getId());

                    List<Recipe> recipes = viewModel.getRecipeList();
                    recipes.remove(getArguments().getInt("recipeIndex", -1));

                    Bundle bundle = new Bundle();
                    bundle.putInt("deleteIndex", getArguments().getInt("recipeIndex", -1));
                    RecipesFragment fragment = new RecipesFragment();
                    fragment.setArguments(bundle);

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                });

                LinearLayout utensilContainer = view.findViewById(R.id.recipe_utensil_container);
                List<Utensil> utensilList = recipe.getUtensilList();
                if (utensilList == null || utensilList.isEmpty()) {
                    TextView emptyText = new TextView(requireContext());
                    emptyText.setText("No utensils");
                    utensilContainer.addView(emptyText);
                } else {
                    for (Utensil utensil : utensilList) {
                        TextView textView = new TextView(requireContext());
                        textView.setText("- " + utensil.getQuantity() + " x " + utensil.getName());
                        utensilContainer.addView(textView);
                    }
                }

                LinearLayout ingredientContainer = view.findViewById(R.id.recipe_ingredient_container);
                List<Ingredient> ingredientList = recipe.getIngredientList();
                if (ingredientList == null || ingredientList.isEmpty()) {
                    TextView emptyText = new TextView(requireContext());
                    emptyText.setText("No ingredients");
                    ingredientContainer.addView(emptyText);
                } else {
                    for (Ingredient ingredient : ingredientList) {
                        TextView textView = new TextView(requireContext());
                        textView.setText("- " + ingredient.getQuantity() + " " + ingredient.getQuantityType() + " of " + ingredient.getName());
                        ingredientContainer.addView(textView);
                    }
                }

                LinearLayout stepContainer = view.findViewById(R.id.recipe_step_container);
                List<Step> stepList = recipe.getStepList();
                if (stepList == null || stepList.isEmpty()) {
                    TextView emptyText = new TextView(requireContext());
                    emptyText.setText("No steps");
                    stepContainer.addView(emptyText);
                } else {
                    int stepIndex = 1;
                    for (Step step : stepList) {
                        View cardView = inflater.inflate(R.layout.recipe_step_card, stepContainer, false);

                        TextView titleView = cardView.findViewById(R.id.stepTitle);
                        TextView descriptionView = cardView.findViewById(R.id.stepDescription);

                        titleView.setText("Step " + stepIndex + ": " + step.getName());
                        descriptionView.setText(step.getDescription());

                        stepContainer.addView(cardView);
                        stepIndex++;
                    }
                }

                Spinner group_spinner = (Spinner) view.findViewById(R.id.recipe_group_spinner);
                // Create an ArrayAdapter using the string array and a default spinner layout.
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        requireContext(),
                        R.array.recipe_group_array,
                        android.R.layout.simple_spinner_item
                );
                // Specify the layout to use when the list of choices appears.
                // Here using the default spinner layout from android studio.
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner.
                group_spinner.setAdapter(adapter);

                String targetGroup = recipe.getGroup();
                if (targetGroup != null) {
                    int position = adapter.getPosition(targetGroup);
                    if (position >= 0) {
                        group_spinner.setSelection(position);
                    }
                }

                group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String selectedItem = parent.getItemAtPosition(pos).toString();
                        if (!selectedItem.equals(recipe.getGroup())) {
                            kitchenDB.setRecipeGroupName(recipe.getId(), selectedItem);
                            recipe.setGroup(selectedItem);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle case where nothing is selected if needed
                    }
                });
            }
        }

        return view;
    }

    private void updateFavoriteIcon(ImageView favView, boolean isFavorite) {
        int iconRes = isFavorite ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_48;
        favView.setImageDrawable(ContextCompat.getDrawable(requireContext(), iconRes));
    }
}