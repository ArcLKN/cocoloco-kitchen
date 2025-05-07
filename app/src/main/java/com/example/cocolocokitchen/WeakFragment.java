package com.example.cocolocokitchen;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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

import java.util.Calendar;
import java.util.Locale;

public class WeakFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        Button selectStartDateBtn = rootView.findViewById(R.id.select_start_date_btn);
        TextView selectedStartDate = rootView.findViewById(R.id.selected_start_date);
        final String[] startDate = {null};

        Spinner spinner = rootView.findViewById(R.id.week_template_spinner);
        Button applyBtn = rootView.findViewById(R.id.apply_week_template_btn);
        TextView planningDetails = rootView.findViewById(R.id.planning_details);
        CalendarView calendarView = rootView.findViewById(R.id.calendar);

        String[] fakeWeekTemplates = {"Healthy weak", "Vegetarian weak", "Fat weak", "Sweat weak" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, fakeWeekTemplates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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

        applyBtn.setOnClickListener(v -> {
            if (startDate[0] == null) {
                planningDetails.setText("Veuillez choisir une date de début !");
                return;
            }

            String selectedTemplate = spinner.getSelectedItem().toString();

            KitchenDB dbHelper = new KitchenDB(getContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String[] times = {"Matin", "Midi", "Goûter", "Soir"};

            for (String time : times) {
                ContentValues values = new ContentValues();
                values.put(KitchenDB.PLANNING_COLUMN_DATE, startDate[0]);
                values.put(KitchenDB.PLANNING_COLUMN_TIME, time);
                values.put(KitchenDB.PLANNING_COLUMN_WEEK_TEMPLATE_ID, 1); // Adapter si nécessaire
                values.put(KitchenDB.RECIPE_COLUMN_ID, 1); // Adapter si nécessaire

                db.insert(KitchenDB.PLANNING_TABLE_NAME, null, values);
            }

            planningDetails.setText("Semaine enregistrée pour " + startDate[0]);
            Toast.makeText(getContext(), "Semaine enregistrée avec succès !", Toast.LENGTH_SHORT).show();

            getParentFragmentManager().popBackStack(); // Retour à l'écran précédent
        });

        return rootView;
    }

}