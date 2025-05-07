package com.example.cocolocokitchen;

public class Utensil {
    private String title;
    private int number;

    public Utensil(String title, int number) {
        this . title = title;
        this . number = number;
    }

    public String getName() {
        return title;
    }
    public int getQuantity() {
        return number;
    }
}
