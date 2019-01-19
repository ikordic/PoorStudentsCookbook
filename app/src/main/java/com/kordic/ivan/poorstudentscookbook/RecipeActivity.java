package com.kordic.ivan.poorstudentscookbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kordic.ivan.poorstudentscookbook.Model.Recipe;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

public class RecipeActivity extends AppCompatActivity
{
    //16:00 - 19:00
    //Getting a Firebase instance - why?

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference recipeRef = db.collection("recipes").document();

    private EditText editTextRecipeName;
    private EditText editTextRecipeDescription;

    private Button buttonSaveRecipe;
    private Button buttonLoadRecipe;
    private Button buttonDeleteRecipe;

    private TextView textViewRecipeName;
    private TextView textViewRecipeDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        this.editTextRecipeName = findViewById(R.id.editTextRecipeName);
        this.editTextRecipeDescription = findViewById(R.id.editTextRecipeDescription);
        this.buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe);
        this.buttonLoadRecipe = findViewById(R.id.buttonLoadRecipe);
        this.buttonDeleteRecipe = findViewById(R.id.buttonDeleteRecipe);
        this.textViewRecipeName = findViewById(R.id.textViewRecipeName);
        this.textViewRecipeDescription = findViewById(R.id.textViewDescription);

        //Save recipe
        buttonSaveRecipe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name = editTextRecipeName.getText().toString();
                String description = editTextRecipeDescription.getText().toString();
                String image = "";

                if (name.isEmpty() || description.isEmpty())
                {
                    Toast.makeText(RecipeActivity.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Recipe recipe = new Recipe(name, description, image);
                recipeRef.set(recipe)
                        .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Toast.makeText(RecipeActivity.this, "Recipe added successfuly", Toast.LENGTH_SHORT).show();
                                editTextRecipeName.setText("");
                                editTextRecipeDescription.setText("");
                                UIUtil.hideKeyboard(RecipeActivity.this);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(RecipeActivity.this, "Adding recipe failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //Load recipe
        buttonLoadRecipe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                recipeRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                        {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot)
                            {
                                if (documentSnapshot.exists())
                                {
                                    Recipe recipe = documentSnapshot.toObject(Recipe.class);
                                    String name = recipe.getRecipeName();
                                    String description = recipe.getRecipeDescription();

                                    textViewRecipeName.setText(name);
                                    textViewRecipeDescription.setText(description);
                                } else
                                {
                                    Toast.makeText(RecipeActivity.this, "Document doesn't exist.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(RecipeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //Delete recipe
        buttonDeleteRecipe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                recipeRef.delete();
                Toast.makeText(RecipeActivity.this, "Recipe deleted.", Toast.LENGTH_SHORT).show();
                textViewRecipeName.setText("");
                textViewRecipeDescription.setText("");
            }
        });

        //Temporary switch activity
        buttonDeleteRecipe.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                startActivity(new Intent(RecipeActivity.this, RecipeCardViewActivity.class));
                return false;
            }
        });
    }
}
