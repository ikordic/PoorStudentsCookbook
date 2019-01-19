package com.kordic.ivan.poorstudentscookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kordic.ivan.poorstudentscookbook.Adapter.RecipeAdapter;
import com.kordic.ivan.poorstudentscookbook.Model.Recipe;

//12:00 - 15:35

public class RecipeCardViewActivity extends AppCompatActivity
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef = db.collection("Recipe");

    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_card_view);

        /*
        Intent i = new Intent(RecipeCardViewActivity.this, RegisterActivity.class);
        startActivity(i);
*/

        //Connecting the adapter and recyclerview
        setUpRecyclerView();
    }

    //Connecting the adapter and recyclerview
    private void setUpRecyclerView()
    {
        Query query = recipeRef.orderBy("recipeName");

        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                .setQuery(query, Recipe.class)
                .build();
        adapter = new RecipeAdapter(options);

        RecyclerView recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        recyclerViewRecipes.setHasFixedSize(true);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecipes.setAdapter(adapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}
