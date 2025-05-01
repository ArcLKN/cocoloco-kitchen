package com.example.cocolocokitchen;

import java.util.List;

public class Recipe {
    private String title;
    private String description;
    private int numberOfServing;
    private int timeInMinute;
    private String costDegree;
    private String imageUrl; // or image resource ID if you're using drawables
    private int imageResId;  // Drawable resource ID (optional, use if needed)
    private List<Ingredient> ingredientList;
    private List<Utensil> utensilList;
    private boolean isFavorite;
    private List<String> tags;
    private String group;

    public Recipe(String title, String description, int numberOfServing, int timeInMinute,
                  String costDegree, String imageUrl, int imageResId, List<Ingredient> ingredientList,
                  List<Utensil> utensilList, boolean isFavorite, List<String> tags, String group) {
        this . title = title;
        this . description = description;
        this . numberOfServing = numberOfServing;
        this . timeInMinute = timeInMinute;
        this . costDegree = costDegree;
        this . imageUrl = imageUrl;
        this . imageResId = imageResId;
        this . ingredientList = ingredientList;
        this . utensilList = utensilList;
        this . isFavorite = isFavorite;
        this . tags = tags;
        this . group = group;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getNumberOfServing() {
        return numberOfServing;
    }

    public int getTimeInMinute() {
        return timeInMinute;
    }

    public String getCostDegree() {
        return costDegree;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Getter for imageResId
    public int getImageResId() {
        return imageResId;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public List<Utensil> getUtensilList() {
        return utensilList;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getGroup() {
        return group;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumberOfServing(int numberOfServing) {
        this.numberOfServing = numberOfServing;
    }

    public void setTimeInMinute(int timeInMinute) {
        this.timeInMinute = timeInMinute;
    }

    public void setCostDegree(String costDegree) {
        this.costDegree = costDegree;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void setUtensilList(List<Utensil> utensilList) {
        this.utensilList = utensilList;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
