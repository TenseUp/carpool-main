package com.example.carpool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore firestore;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();



        TextView email = findViewById(R.id.UserEmail);
        TextView type = findViewById(R.id.UserProfileType);

        firestore.collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser = new User();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            currentUser = document.toObject(User.class);

                            if(mUser.getEmail().equals(currentUser.getEmail()))
                            {
                                type.setText(currentUser.getType());
                                email.setText(mUser.getEmail());
                            }
                        }
                    }
                });
    }
   public void signOut(View v)
   {
       FirebaseAuth.getInstance().signOut();
       Intent intent = new Intent(this, AuthActivity.class);
       startActivity(intent);
   }

   public void seeVehicles(View v)
   {
       Intent intent = new Intent(this, VehicleInfoActivity.class);
       startActivity(intent);
   }

   public void addVehicleButton(View v)
   {
       Intent intent = new Intent(this, AddVehicleActivity.class);
       startActivity(intent);
   }

}