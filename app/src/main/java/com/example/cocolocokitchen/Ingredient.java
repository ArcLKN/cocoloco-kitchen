package com.example.cocolocokitchen;

import java.util.List;

public class Ingredient {
    private String title;
    private String number;
    private String numberType;

    public Ingredient(String title, String number, String numberType) {
        this . title = title;
        this . number = number;
        this . numberType = numberType;
    }

    public String getName() {
        return title;
    }
    public String getQuantity() {
        return number;
    }
    public String getQuantityType() {
        return numberType;
    }
}
