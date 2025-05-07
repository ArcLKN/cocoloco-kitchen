package com.example.cocolocokitchen;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

public class HomeFragment extends Fragment {
    private EditText editTextIngredient;
    private Button buttonAdd;
    private LinearLayout checkboxContainer;
    private KitchenDB kitchenDB;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        editTextIngredient = rootView.findViewById(R.id.editTextIngredient);
        buttonAdd = rootView.findViewById(R.id.buttonAdd);
        checkboxContainer = rootView.findViewById(R.id.checkboxContainer);

        kitchenDB = new KitchenDB(getContext());

        buttonAdd.setOnClickListener(v -> addIngredientToList());

        Button btnMore = rootView.findViewById(R.id.moreButton);

        // On définit le comportement au clic
        btnMore.setOnClickListener(v -> {
            Fragment CalendarFragment2 = new CalendarFragment2();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, CalendarFragment2);
            transaction.addToBackStack(null);
            transaction.commit();
        });
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