package com.kordic.ivan.poorstudentscookbook;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;
//12:50 - 15:10
public class RegisterActivity extends AppCompatActivity
{
    //FirebaseAuth instance
    private FirebaseAuth userAuth;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private Button buttonLogin;
    private Button buttonShow;
    private Button buttonLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //FirebaseAuth initialization
        userAuth = FirebaseAuth.getInstance();

        this.editTextEmail = findViewById(R.id.editTextEmail);
        this.editTextPassword = findViewById(R.id.editTextPassword);

        this.buttonRegister = findViewById(R.id.buttonRegister);
        this.buttonLogin = findViewById(R.id.buttonLogin);
        this.buttonShow = findViewById(R.id.buttonShow);
        this.buttonLogOut = findViewById(R.id.buttonLogOut);


        //Registration
        buttonRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                //Hide keyboard after click on buttonRegister
                UIUtil.hideKeyboard(RegisterActivity.this);

                //Crash prevention if fields are null
                if (email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Create new user
                userAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    editTextEmail.setText("");
                                    editTextPassword.setText("");
                                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(RegisterActivity.this, new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //Login
        buttonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                //Hide keyboard after click on buttonLogin
                UIUtil.hideKeyboard(RegisterActivity.this);

                //Crash prevention if fields are null
                if (email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Sign in user
                userAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<AuthResult>()
                        {
                            @Override
                            public void onSuccess(AuthResult authResult)
                            {
                                editTextEmail.setText("");
                                editTextPassword.setText("");
                                Toast.makeText(RegisterActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(RegisterActivity.this, new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //Log out the user
        buttonLogOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                userAuth.signOut();
                Toast.makeText(RegisterActivity.this, "Signed out!", Toast.LENGTH_SHORT).show();
            }
        });

        //Show email of logged user
        buttonShow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                {
                    Toast.makeText(RegisterActivity.this, "Korisnik: " + user.getEmail(), Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(RegisterActivity.this, "No signed-in user!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
