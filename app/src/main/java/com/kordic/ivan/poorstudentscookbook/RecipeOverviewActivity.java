package com.kordic.ivan.poorstudentscookbook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kordic.ivan.poorstudentscookbook.Model.Recipe;

public class RecipeOverviewActivity extends AppCompatActivity
{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef = db.collection("Recipe");
    
    private TextView textViewRecipeOverviewName;
    private TextView textViewRecipeOverviewDescription;    
    private ImageView imageViewRecipeOverview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);
        
        this.textViewRecipeOverviewName = findViewById(R.id.textViewRecipeOverviewName);
        this.textViewRecipeOverviewDescription = findViewById(R.id.textViewRecipeOverviewDescription);
        this.imageViewRecipeOverview = findViewById(R.id.imageViewRecipeOverview);

        String recipeId = "";
        //why saved instance?
        if(savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if(extras == null)
            {
                recipeId = "prazan";
            }
            else
            {
                recipeId = extras.getString("RECIPE_ID");
            }
        }
        else
        {
            recipeId= (String) savedInstanceState.getSerializable("RECIPE_ID");
        }
        
        if(recipeId.isEmpty())
        {
            Toast.makeText(this, "Critical error!", Toast.LENGTH_SHORT).show();
            return;
        }
        recipeRef.document(recipeId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        if(documentSnapshot.exists())
                        {
                            Recipe recipe = documentSnapshot.toObject(Recipe.class);
                            textViewRecipeOverviewName.setText(recipe.getRecipeName());
                            textViewRecipeOverviewDescription.setText(recipe.getRecipeDescription());
                            Glide.with(RecipeOverviewActivity.this).load(recipe.getRecipeImage()).into(imageViewRecipeOverview);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(RecipeOverviewActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
