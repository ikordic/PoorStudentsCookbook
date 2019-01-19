package com.kordic.ivan.poorstudentscookbook.Model;

//Model class which stores data

public class Recipe
{
    private String recipeName;
    private String recipeDescription;
    private String recipeImage;

    //Constructors
    public Recipe()
    {
    }

    public Recipe(String recipeName, String recipeDescription, String recipeImage)
    {
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

    public String getRecipeImage()
    {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage)
    {
        this.recipeImage = recipeImage;
    }
}
