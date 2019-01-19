package com.kordic.ivan.poorstudentscookbook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;

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
    private Button buttonAddNewRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_card_view);

        this.buttonAddNewRecipe = findViewById(R.id.buttonAddNewRecipe);

        //startActivity(new Intent(RecipeCardViewActivity.this, RegisterActivity.class));

        //Connecting the adapter and recyclerview
        setUpRecyclerView();

        buttonAddNewRecipe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(RecipeCardViewActivity.this, AddNewRecipeActivity.class));
            }
        });
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1)
            {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerViewRecipes);
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
