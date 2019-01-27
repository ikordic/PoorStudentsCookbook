package com.kordic.ivan.poorstudentscookbook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kordic.ivan.poorstudentscookbook.Model.Recipe;
import com.kordic.ivan.poorstudentscookbook.Model.User;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity
{

    private FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("User");

    private ImageView imageViewUserProfileImage;
    private TextView textViewUserProfileUsername;
    private EditText editTextUserProfileEmail;
    private EditText editTextUserProfilePassword;
    private TextView textViewUserProfileChangeEmail;
    private TextView textViewUserProfileChangePassword;
    private TextView textViewUserProfileDeleteProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        this.imageViewUserProfileImage = findViewById(R.id.imageViewUserProfileImage);
        this.textViewUserProfileUsername = findViewById(R.id.textViewUserProfileUsername);
        this.editTextUserProfileEmail = findViewById(R.id.editTextUserProfileEmail);
        this.editTextUserProfilePassword = findViewById(R.id.editTextUserProfilePassword);
        this.textViewUserProfileChangeEmail = findViewById(R.id.textViewUserProfileChangeEmail);
        this.textViewUserProfileChangePassword = findViewById(R.id.textViewUserProfileChangePassword);
        this.textViewUserProfileDeleteProfile = findViewById(R.id.textViewUserProfileDeleteProfile);

        //Keep the keyboard down
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        userRef.document(Objects.requireNonNull(userAuth.getUid())).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
                {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        User user = documentSnapshot.toObject(User.class);
                        assert user != null;
                        Glide.with(UserProfileActivity.this).load(user.getUserProfileImage()).into(imageViewUserProfileImage);
                        textViewUserProfileUsername.setText(user.getUserUsername());
                        editTextUserProfileEmail.setText(Objects.requireNonNull(userAuth.getCurrentUser()).getEmail());
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(UserProfileActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
