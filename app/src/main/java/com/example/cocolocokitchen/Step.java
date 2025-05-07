package com.example.cocolocokitchen;

public class Step {
    private String title;
    private String description;

    public Step(String title, String description) {
        this . title = title;
        this . description = description;
    }

    public String getName() {
        return title;
    }
    public String getDescription() {
        return description;
    }
}
