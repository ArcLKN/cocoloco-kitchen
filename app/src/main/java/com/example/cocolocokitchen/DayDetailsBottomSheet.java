package com.example.cocolocokitchen;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DayDetailsBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_DATE = "date";

    public static DayDetailsBottomSheet newInstance(String date) {
        DayDetailsBottomSheet fragment = new DayDetailsBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_day_details, container, false);

        String date = getArguments() != null ? getArguments().getString(ARG_DATE) : null;
        if (date == null) {
            Toast.makeText(requireContext(), "Date invalide", Toast.LENGTH_SHORT).show();
            dismiss(); // ferme le bottom sheet si pas de date
            return view;
        }

        TextView weekTypeText = view.findViewById(R.id.week_type);
        TextView matinText = view.findViewById(R.id.meal_matin);
        TextView midiText = view.findViewById(R.id.meal_midi);
        TextView gouterText = view.findViewById(R.id.meal_gouter);
        TextView soirText = view.findViewById(R.id.meal_soir);

        // Sécurité si une vue est manquante
        if (weekTypeText == null || matinText == null || midiText == null || gouterText == null || soirText == null) {
            Toast.makeText(requireContext(), "Erreur d'affichage : layout incomplet", Toast.LENGTH_LONG).show();
            dismiss();
            return view;
        }

        try {
            Context context = requireContext();
            KitchenDB dbHelper = new KitchenDB(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT " + KitchenDB.PLANNING_COLUMN_TIME + ", " +
                            KitchenDB.RECIPE_COLUMN_ID + ", " +
                            KitchenDB.PLANNING_COLUMN_WEEK_TEMPLATE_ID +
                            " FROM " + KitchenDB.PLANNING_TABLE_NAME +
                            " WHERE " + KitchenDB.PLANNING_COLUMN_DATE + " = ?",
                    new String[]{date});

            String weekType = "Inconnue";

            if (cursor.moveToFirst()) {
                do {
                    String time = cursor.getString(0);
                    long recipeId = cursor.getLong(1);
                    int weekTemplateId = cursor.getInt(2);

                    switch (time) {
                        case "Matin":
                            matinText.setText("Recette #" + recipeId);
                            break;
                        case "Midi":
                            midiText.setText("Recette #" + recipeId);
                            break;
                        case "Goûter":
                            gouterText.setText("Recette #" + recipeId);
                            break;
                        case "Soir":
                            soirText.setText("Recette #" + recipeId);
                            break;
                    }

                    weekType = "ID #" + weekTemplateId;
                } while (cursor.moveToNext());
            } else {
                matinText.setText("(aucune recette)");
                midiText.setText("(aucune recette)");
                gouterText.setText("(aucune recette)");
                soirText.setText("(aucune recette)");
            }

            weekTypeText.setText("Semaine type : " + weekType);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return view;
    }
}
