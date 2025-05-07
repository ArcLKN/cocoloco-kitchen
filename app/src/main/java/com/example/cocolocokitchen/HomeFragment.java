package com.example.cocolocokitchen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // On gonfle la vue du fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnMore = view.findViewById(R.id.moreButton);

        // On définit le comportement au clic
        btnMore.setOnClickListener(v -> {
            Fragment CalendarFragment2 = new CalendarFragment2();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, CalendarFragment2);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
}