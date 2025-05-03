package com.example.cocolocokitchen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateRecipeFragment newInstance(String param1, String param2) {
        CreateRecipeFragment fragment = new CreateRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View view = inflater.inflate(R.layout.fragment_create_recipe, container, false);

        Button createRecipeButton = view.findViewById(R.id.create_recipe_button);
        createRecipeButton.setOnClickListener(v -> {
            createRecipe(view);
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void createRecipe(View view) {

        // Get the input data
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
            // Handle invalid input (e.g., show an error)
        }

        EditText edit_text_minutes = view.findViewById(R.id.create_recipe_minutes);
        int minutes = 0;
        try {
            minutes = Integer.parseInt(edit_text_minutes.getText().toString().trim());
        } catch (NumberFormatException e) {
            // Handle invalid input
        }

        int time = hours * 60 + minutes;

        EditText edit_text_people = view.findViewById(R.id.create_recipe_people);
        int people = 0;
        try {
            people = Integer.parseInt(edit_text_people.getText().toString().trim());
        } catch (NumberFormatException e) {
            // Handle invalid input
        }

        EditText edit_text_source = view.findViewById(R.id.create_recipe_source);
        String source = edit_text_source.getText().toString().trim();

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Recipe> recipes = viewModel.getRecipeList();

        Recipe new_recipe = new Recipe(title, desc, people, time, "$", null, R.drawable.recipe_default, null, null, false, null, null);

        recipes.add(new_recipe);

        // Show confirmation toast
        Toast.makeText(getContext(), "New Recipe Created!", Toast.LENGTH_SHORT).show();
    }
}