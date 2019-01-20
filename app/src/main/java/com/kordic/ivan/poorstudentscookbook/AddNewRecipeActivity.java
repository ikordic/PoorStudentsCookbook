package com.kordic.ivan.poorstudentscookbook;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kordic.ivan.poorstudentscookbook.Model.Recipe;

public class AddNewRecipeActivity extends AppCompatActivity
{

    //16:00 - 17:10

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef = db.collection("Recipe");
    private StorageReference recipeStorageRef;

    private EditText editTextNewRecipeName;
    private EditText editTextNewRecipeDescription;
    //private EditText editTextNewRecipeImageUrl;
    private Button buttonSaveNewRecipe;
    private ImageView imageViewNewRecipe;
    private TextView textViewAddFromDevice;

    //Constant for image selection
    private static final int PICK_IMAGE_REQUEST = 1;
    //URI which points to image for the imageview
    private Uri recipeImageUri;
    String newRecipeImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);

        recipeStorageRef = FirebaseStorage.getInstance().getReference("recipe");

        //keep the keyboard down
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.editTextNewRecipeName = findViewById(R.id.editTextNewRecipeName);
        this.editTextNewRecipeDescription = findViewById(R.id.editTextNewRecipeDescription);
        //this.editTextNewRecipeImageUrl = findViewById(R.id.editTextNewRecipeImageUrl);
        this.buttonSaveNewRecipe = findViewById(R.id.buttonSaveNewRecipe);
        this.imageViewNewRecipe = findViewById(R.id.imageViewNewRecipe);
        this.textViewAddFromDevice = findViewById(R.id.textViewAddFromDevice);

        textViewAddFromDevice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Gets only images and the constant is used to identify the activity
                startActivityForResult(new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), PICK_IMAGE_REQUEST);
            }
        });

        buttonSaveNewRecipe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String newRecipeName = editTextNewRecipeName.getText().toString();
                String newRecipeDescription = editTextNewRecipeDescription.getText().toString();


                if(newRecipeName.trim().isEmpty() || newRecipeDescription.trim().isEmpty())
                {
                    Toast.makeText(AddNewRecipeActivity.this, "Recipe name and description must be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
/*
                recipeStorageRef.putFile(recipeImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return recipeStorageRef.getDownloadUrl();
                    }
               }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Toast.makeText(AddNewRecipeActivity.this, downloadUri.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddNewRecipeActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
*/
/*
                if(recipeImageUri != null)
                {
                    StorageReference fileReference = recipeStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(recipeImageUri));
                    fileReference.putFile(recipeImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    //Toast.makeText(AddNewRecipeActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                    newRecipeImageUrl = recipeStorageRef.child("recipe/1548009924299.jpg").getDownloadUrl().toString();
                                    Toast.makeText(AddNewRecipeActivity.this, newRecipeImageUrl, Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Toast.makeText(AddNewRecipeActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    //Toast.makeText(AddNewRecipeActivity.this, "Uploading...", Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(AddNewRecipeActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
                }
*/
                //Firestore add
                recipeRef
                        .add(new Recipe(newRecipeName, newRecipeDescription, newRecipeImageUrl))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                        {
                            @Override
                            public void onSuccess(DocumentReference documentReference)
                            {
                                //Toast.makeText(AddNewRecipeActivity.this, "Recipe added!", Toast.LENGTH_SHORT).show();
                                //finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            recipeImageUri = data.getData();
            Glide.with(this).load(recipeImageUri).into(imageViewNewRecipe);
        }
    }

    //Get file extension
    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

