package com.example.carpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText emailF;
    private EditText passwordF;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        spinner = findViewById(R.id.spinner_auth);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.UserType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        emailF = findViewById(R.id.email);
        passwordF = findViewById(R.id.password);
    }

    public void signIn(View v)
    {
        String emailS = emailF.getText().toString();
        String passwordS = passwordF.getText().toString();

        mAuth.signInWithEmailAndPassword(emailS, passwordS)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


    public void signUp(View v)
    {
        String emailS = emailF.getText().toString();
        String passwordS = passwordF.getText().toString();

        if(!emailS.contains("@cis.edu.hk"))
            Toast.makeText(getApplicationContext(), "please enter a valid CIS email", Toast.LENGTH_SHORT).show();
        else {
            mAuth.createUserWithEmailAndPassword(emailS, passwordS).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    uploadData(user);
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            });
        }
    }

    public void updateUI(FirebaseUser currentUser)
    {
        if(currentUser != null)
        {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }
    }
    public void uploadData(FirebaseUser currentUser)
    {
        String emailS = currentUser.getEmail();
        String typeS = spinner.getSelectedItem().toString();

        Random r = new Random();
        int id = r.nextInt(99999);

        User use = new User(id, emailS, typeS);

        firestore.collection("User").add(use);
    }
}