package com.kordic.ivan.poorstudentscookbook.Model;

//Model class which stores data

import java.util.ArrayList;

public class Recipe
{
    private String recipeName;
    private String recipeDescription;
    private String recipeImage;
    private String creator;

    private ArrayList<String> ingredients;



    //Constructors
    public Recipe()
    {
    }


    public Recipe(String recipeName, String recipeDescription, String recipeImage, String creator, ArrayList ingredients)
    {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.recipeImage = recipeImage;
        this.creator = creator;
        this.ingredients = new ArrayList<String>();
    }

    public Recipe(String recipeName, String recipeDescription, String recipeImage) {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.recipeImage = recipeImage;
    }

    //Getters-Setters
    public String getRecipeName()
    {
        return recipeName;
    }

    public void setRecipeName(String recipeName)
    {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription()
    {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription)
    {
        this.recipeDescription = recipeDescription;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }
    public String getRecipeImage()
    {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage)
    {
        this.recipeImage = recipeImage;
    }
}
