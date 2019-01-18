package com.kordic.ivan.poorstudentscookbook.Model;

public class Recipe
{
    private String recipeName;
    private String recipeDescription;

    //Constructors
    public Recipe()
    {
    }

    public Recipe(String recipeName, String recipeDescription)
    {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
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
}
