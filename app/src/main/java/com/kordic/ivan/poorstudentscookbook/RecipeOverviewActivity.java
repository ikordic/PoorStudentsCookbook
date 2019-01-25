package com.kordic.ivan.poorstudentscookbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private TextView textViewRecipeOverviewBy;

    private String recipeId = "";
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_overview);
        
        this.textViewRecipeOverviewName = findViewById(R.id.textViewRecipeOverviewName);
        this.textViewRecipeOverviewDescription = findViewById(R.id.textViewRecipeOverviewDescription);
        this.imageViewRecipeOverview = findViewById(R.id.imageViewRecipeOverview);
        this.textViewRecipeOverviewBy = findViewById(R.id.textViewRecipeOverviewBy);

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
                            assert recipe != null;
                            textViewRecipeOverviewName.setText(recipe.getRecipeName());
                            textViewRecipeOverviewDescription.setText(recipe.getRecipeDescription());
                            textViewRecipeOverviewBy.setText("by: " + recipe.getRecipeAuthorUsername());
                            Glide.with(RecipeOverviewActivity.this).load(recipe.getRecipeImage()).into(imageViewRecipeOverview);

                            username = recipe.getRecipeAuthorUsername();
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

        textViewRecipeOverviewBy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(RecipeOverviewActivity.this, ProfileRecipesActivity.class).putExtra("USERNAME", username));
            }
        });
    }
}
