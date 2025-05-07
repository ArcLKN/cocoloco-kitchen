package com.example.cocolocokitchen;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                groupText.setText("" + recipe.getGroup());

                TextView costText = view.findViewById(R.id.recipe_cost);
                costText.setText(recipe.getCostDegree());

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
                        TextView titleView = new TextView(requireContext());
                        titleView.setText("Step " + stepIndex + ": " + step.getName());
                        titleView.setTypeface(null, Typeface.BOLD);

                        TextView descriptionView = new TextView(requireContext());
                        descriptionView.setText(step.getDescription());

                        stepContainer.addView(titleView);
                        stepContainer.addView(descriptionView);
                        stepIndex++;
                    }
                }
            }
        }

        return view;
    }
}