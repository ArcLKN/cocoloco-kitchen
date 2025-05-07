package com.example.cocolocokitchen;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeFragment extends Fragment {
    private EditText editTextIngredient;
    private Button buttonAdd;
    private LinearLayout checkboxContainer;
    private KitchenDB kitchenDB;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        editTextIngredient = rootView.findViewById(R.id.editTextIngredient);
        buttonAdd = rootView.findViewById(R.id.buttonAdd);
        checkboxContainer = rootView.findViewById(R.id.checkboxContainer);

        // Initialize the database handler
        kitchenDB = new KitchenDB(getContext());

        // Set up button click listener
        buttonAdd.setOnClickListener(v -> addIngredientToList());

        return rootView;
    }

    private void insertIngredientIntoDatabase(String ingredientName) {
        SQLiteDatabase db = kitchenDB.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KitchenDB.INGREDIENT_COLUMN_NAME, ingredientName);

        long newRowId = db.insert(KitchenDB.INGREDIENT_TABLE_NAME, null, values);

        if (newRowId != -1) {
            Toast.makeText(getContext(), "Ingredient added to database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to add ingredient to database", Toast.LENGTH_SHORT).show();
        }
    }

    private void addIngredientToList() {
        String ingredientName = editTextIngredient.getText().toString().trim();

        if (!ingredientName.isEmpty()) {
            insertIngredientIntoDatabase(ingredientName);

            CheckBox newCheckBox = new CheckBox(getContext());
            newCheckBox.setText(ingredientName);
            newCheckBox.setTag(ingredientName);

            // Add listener for when checkbox is checked
            newCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkboxContainer.removeView(buttonView);
                    removeIngredientFromDatabase(ingredientName);
                }
            });

            checkboxContainer.addView(newCheckBox);
            editTextIngredient.setText("");
        } else {
            Toast.makeText(getContext(), "Please enter an ingredient.", Toast.LENGTH_SHORT).show();
        }
    }

    // ✅ FIXED: Method is outside of any other method
    private void removeIngredientFromDatabase(String ingredientName) {
        SQLiteDatabase db = kitchenDB.getWritableDatabase();

        int rowsDeleted = db.delete(KitchenDB.INGREDIENT_TABLE_NAME,
                KitchenDB.INGREDIENT_COLUMN_NAME + " = ?", new String[]{ingredientName});

        if (rowsDeleted > 0) {
            Toast.makeText(getContext(), "Ingredient removed from database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to remove ingredient from database", Toast.LENGTH_SHORT).show();
        }
    }
}