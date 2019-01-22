package com.kordic.ivan.poorstudentscookbook;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kordic.ivan.poorstudentscookbook.Model.Recipe;

import java.util.Objects;

public class AddNewRecipeActivity extends AppCompatActivity
{
    //17:00 - 01:00

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference recipeRef = db.collection("Recipe");
    private StorageReference recipeStorageRef;

    private EditText editTextNewRecipeName;
    private EditText editTextNewRecipeDescription;
    private Button buttonSaveNewRecipe;
    private ImageView imageViewNewRecipe;
    private TextView textViewAddFromDevice;
    private ProgressBar progressBar;

    ViewGroup progressView;
    protected boolean isProgressShowing = false;

    //Constant for image selection
    private static final int PICK_IMAGE_REQUEST = 1;
    //URI which points to image for the imageview
    private Uri recipeImageUri;

    private Task recipeUploadTask;

    //Global variables with default values
    String newRecipeName;
    String newRecipeDescription;
    String newRecipeImageUrl = "https://firebasestorage.googleapis.com/v0/b/poorstudentscookbook-f9e8b.appspot.com/o/recipe%2Flogo_600.png?alt=media&token=0c838482-d339-4724-a671-e76366b7d894";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);

        recipeStorageRef = FirebaseStorage.getInstance().getReference("recipe");

        //Keep the keyboard down
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        this.editTextNewRecipeName = findViewById(R.id.editTextNewRecipeName);
        this.editTextNewRecipeDescription = findViewById(R.id.editTextNewRecipeDescription);
        this.buttonSaveNewRecipe = findViewById(R.id.buttonSaveNewRecipe);
        this.imageViewNewRecipe = findViewById(R.id.imageViewNewRecipe);
        this.textViewAddFromDevice = findViewById(R.id.textViewAddFromDevice);
        this.progressBar = findViewById(R.id.progressBar);

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
                //if task running check**

                newRecipeName = editTextNewRecipeName.getText().toString();
                newRecipeDescription = editTextNewRecipeDescription.getText().toString();

                if (newRecipeName.trim().isEmpty() || newRecipeDescription.trim().isEmpty())
                {
                    Toast.makeText(AddNewRecipeActivity.this, "Fill all text fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                    uploadFile();

            }
        });
    }

    //ProgressBar
    public void showProgressingView() {

        if (!isProgressShowing) {
            isProgressShowing = true;
            progressView = (ViewGroup) getLayoutInflater().inflate(R.layout.progressbar_layout, null);
            View v = this.findViewById(android.R.id.content).getRootView();
            ViewGroup viewGroup = (ViewGroup) v;
            viewGroup.addView(progressView);
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }

    //Loads image into imageView after selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
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

    //Uploads all data and fetches the firebase storage image url
    private void uploadFile()
    {
        //Anti-button-spam method
        buttonSaveNewRecipe.setClickable(false);
        showProgressingView();

        if (recipeImageUri != null)
        {
            final StorageReference fileReference = recipeStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(recipeImageUri));

            recipeUploadTask = fileReference.putFile(recipeImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downUri = task.getResult();
                        newRecipeImageUrl = String.valueOf(downUri);

                        recipeRef
                                .add(new Recipe(newRecipeName, newRecipeDescription, Objects.requireNonNull(task.getResult()).toString()))
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
                    else
                    {
                        Toast.makeText(AddNewRecipeActivity.this, "Task failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(AddNewRecipeActivity.this, "putFile() doesn't work", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
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
    }
}

