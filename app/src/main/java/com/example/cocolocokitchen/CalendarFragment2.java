package com.example.cocolocokitchen;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import java.util.Locale;

public class CalendarFragment2 extends Fragment {

    public CalendarFragment2() {} // Constructeur correct

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar2, container, false);


        Button btnMore = view.findViewById(R.id.btn_add_weak);

        btnMore.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_weak, new WeakFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Ajoute la gestion du calendrier
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                DayDetailsBottomSheet bottomSheet = DayDetailsBottomSheet.newInstance(dateStr);
                bottomSheet.show(getParentFragmentManager(), "DayDetailsBottomSheet");
            }
        });

        return view;
    }
}