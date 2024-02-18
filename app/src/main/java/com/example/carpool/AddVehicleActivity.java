package com.example.carpool;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AddVehicleActivity extends AppCompatActivity {
    private Spinner spinner;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText model;
    private EditText cap;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        spinner = findViewById(R.id.spinner);
        cap = findViewById(R.id.capacity);
        model = findViewById(R.id.model);




        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.VehicleType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void addVehicle(View v) {

        String capS = cap.getText().toString();
        Integer capI = Integer.parseInt(capS);
        String modelS = model.getText().toString();
        String typeS = spinner.getSelectedItem().toString();


        FirebaseUser mUser = mAuth.getCurrentUser();
        Vehicle vehicle = new Vehicle(typeS, capI, modelS, mUser.getEmail());
        firestore.collection("Vehicles").add(vehicle);


        firestore.collection("User")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser = new User();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            currentUser = document.toObject(User.class);
                            if(mUser.getEmail().equals(currentUser.getEmail()))
                            {
                                currentUser.addVehicle(vehicle);
                                updateUser(document.getId(), currentUser);
                            }
                        }
                    }
                });
        Intent intent = new Intent(this, VehicleInfoActivity.class);
        startActivity(intent);

    }
    
        public void updateUser(String docID, User u) {firestore.collection("User").document(docID).set(u);}

}