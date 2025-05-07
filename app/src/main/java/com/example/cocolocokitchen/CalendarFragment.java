package com.example.cocolocokitchen;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    public CalendarFragment() {
        // Constructeur vide
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        Button selectStartDateBtn = rootView.findViewById(R.id.select_start_date_btn);
        TextView selectedStartDate = rootView.findViewById(R.id.selected_start_date);
        final String[] startDate = {null};

        selectStartDateBtn.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(getContext(), (view, y, m, d) -> {
                String dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d);
                startDate[0] = dateStr;
                selectedStartDate.setText("Début : " + dateStr);
            }, year, month, day).show();
        });

        CalendarView calendarView = rootView.findViewById(R.id.calendar);
        Spinner spinner = rootView.findViewById(R.id.week_template_spinner);
        Button applyBtn = rootView.findViewById(R.id.apply_week_template_btn);
        TextView planningDetails = rootView.findViewById(R.id.planning_details);

        String[] fakeWeekTemplates = {"Semaine équilibrée", "Semaine rapide", "Végétarienne"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, fakeWeekTemplates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final String[] selectedDate = {null};

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate[0] = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

            KitchenDB dbHelper = new KitchenDB(getContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT " + KitchenDB.PLANNING_COLUMN_TIME + ", " + KitchenDB.RECIPE_COLUMN_ID +
                            " FROM " + KitchenDB.PLANNING_TABLE_NAME +
                            " WHERE " + KitchenDB.PLANNING_COLUMN_DATE + "=?",
                    new String[]{selectedDate[0]}
            );

            StringBuilder planning = new StringBuilder();
            if (cursor.moveToFirst()) {
                do {
                    String time = cursor.getString(0);
                    long recipeId = cursor.getLong(1);
                    planning.append("• ").append(time).append(" : Recette #").append(recipeId).append("\n");
                } while (cursor.moveToNext());
            } else {
                planning.append("Aucun planning pour ce jour.");
            }
            cursor.close();

            planningDetails.setText(planning.toString());

            DayDetailsBottomSheet bottomSheet = DayDetailsBottomSheet.newInstance(selectedDate[0]);
            bottomSheet.show(getParentFragmentManager(), "DayDetailsBottomSheet");
        });

        applyBtn.setOnClickListener(v -> {
            int pos = spinner.getSelectedItemPosition();
            long fakeTemplateId = pos + 1;

            if (startDate[0] == null) {
                Toast.makeText(getContext(), "Sélectionnez une date de début !", Toast.LENGTH_SHORT).show();
                return;
            }

            KitchenDB dbHelper = new KitchenDB(getContext());
            dbHelper.applyWeekTemplateToCalendar(fakeTemplateId, startDate[0]);

            Toast.makeText(getContext(),
                    "Semaine type appliquée à partir du " + startDate[0],
                    Toast.LENGTH_LONG).show();

            calendarView.setDate(parseDateToMillis(startDate[0]));
        });

        // Bouton de test pour créer une semaine type
        Button testBtn = rootView.findViewById(R.id.test_button);
        testBtn.setOnClickListener(v -> {
            KitchenDB db = new KitchenDB(getContext());

            long weekTemplateId = db.addWeekTemplate("Semaine test");
            db.addRecipeToWeekTemplate(weekTemplateId, 1, "Lundi", "midi");
            db.addRecipeToWeekTemplate(weekTemplateId, 2, "Mardi", "soir");

            String testStartDate = "2025-05-10";
            db.applyWeekTemplateToCalendar(weekTemplateId, testStartDate);

            Toast.makeText(getContext(), "Semaine test appliquée !", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }

    private long parseDateToMillis(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.parse(dateStr).getTime();
        } catch (ParseException e) {
            return System.currentTimeMillis();
        }
    }

    private void showDayDetailsDialog(String date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_day_details, null);
        builder.setView(dialogView);

        TextView dateText = dialogView.findViewById(R.id.day_date);
        dateText.setText("Date : " + date);

        TextView weekTypeText = dialogView.findViewById(R.id.week_type);
        TextView matinText = dialogView.findViewById(R.id.meal_matin);
        TextView midiText = dialogView.findViewById(R.id.meal_midi);
        TextView gouterText = dialogView.findViewById(R.id.meal_gouter);
        TextView soirText = dialogView.findViewById(R.id.meal_soir);

        KitchenDB dbHelper = new KitchenDB(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + KitchenDB.PLANNING_COLUMN_TIME + ", " + KitchenDB.RECIPE_COLUMN_ID +
                        ", " + KitchenDB.PLANNING_COLUMN_WEEK_TEMPLATE_ID +
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
        builder.setPositiveButton("Fermer", null);
        builder.show();
    }
}
