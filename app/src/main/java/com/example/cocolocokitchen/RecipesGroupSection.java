package com.example.cocolocokitchen;

import java.util.List;

public class RecipesGroupSection {
    String groupName;
    String color;
    List<Recipe> recipes;
    boolean isExpanded;

    public RecipesGroupSection (String groupName, String color, List<Recipe> recipes, boolean isExpanded) {
        this . groupName = groupName;
        this . color = color;
        this . recipes = recipes;
        this . isExpanded = isExpanded;
    }

    public void setExpanded(boolean isExpanded) {this .isExpanded = isExpanded;}

    public String getGroupName() {return this . groupName;}
    public String getColor() {return this . color;}
    public List<Recipe> getRecipes() {return this . recipes;}
    public boolean isExpanded() {return this . isExpanded;}
}
