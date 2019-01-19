package com.kordic.ivan.poorstudentscookbook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kordic.ivan.poorstudentscookbook.Model.Recipe;

public class AddNewRecipeActivity extends AppCompatActivity
{

    //16:00 - 17:10

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef = db.collection("Recipe");

    private EditText editTextNewRecipeName;
    private EditText editTextNewRecipeDescription;
    private EditText editTextNewRecipeImageUrl;
    private Button buttonSaveNewRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);

        this.editTextNewRecipeName = findViewById(R.id.editTextNewRecipeName);
        this.editTextNewRecipeDescription = findViewById(R.id.editTextNewRecipeDescription);
        this.editTextNewRecipeImageUrl = findViewById(R.id.editTextNewRecipeImageUrl);
        this.buttonSaveNewRecipe = findViewById(R.id.buttonSaveNewRecipe);

        buttonSaveNewRecipe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String newRecipeName = editTextNewRecipeName.getText().toString();
                String newRecipeDescription = editTextNewRecipeDescription.getText().toString();
                String newRecipeImageUrl = editTextNewRecipeImageUrl.getText().toString();

                if(newRecipeName.trim().isEmpty() || newRecipeDescription.trim().isEmpty())
                {
                    Toast.makeText(AddNewRecipeActivity.this, "Recipe name and description must be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                recipeRef
                        .add(new Recipe(newRecipeName, newRecipeDescription, newRecipeImageUrl))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                        {
                            @Override
                            public void onSuccess(DocumentReference documentReference)
                            {
                                Toast.makeText(AddNewRecipeActivity.this, "Recipe added!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(AddNewRecipeActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
