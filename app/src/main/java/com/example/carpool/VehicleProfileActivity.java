package com.example.carpool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class VehicleProfileActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String model;
    private String capacity;
    private String owner;
    private String type;
    private String open;
    private Integer id;
    private FirebaseFirestore firestore;
    private Vehicle vehicle;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        TextView modelT = findViewById(R.id.vpmodel);
        TextView capacityT = findViewById(R.id.vpcapacity);
        TextView ownerT = findViewById(R.id.vpowner);
        TextView typeT = findViewById(R.id.vptype);
        TextView openT = findViewById(R.id.vpopen);

        Button bookingButton = findViewById(R.id.vpbook);
        Button openButton = findViewById(R.id.vpopenb);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            model = extras.getString("model");
            capacity = extras.getString("capacity");
            owner = extras.getString("owner");
            type = extras.getString("type");
            open = extras.getString("openStatus");
            id = extras.getInt("vID");
        }

        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser.getEmail().equals(owner)) {bookingButton.setVisibility(View.GONE);} else{openButton.setVisibility(View.GONE);}

        modelT.setText(model);
        capacityT.setText(capacity);
        ownerT.setText(owner);
        typeT.setText(type);
        openT.setText(open);
    }

    public void openCloseVehicle(View v)
    {
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("Vehicles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        vehicle = new Vehicle();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            vehicle = document.toObject(Vehicle.class);
                            if(id.equals(vehicle.getID()))
                            {
                                vehicle.setOpen();
                                updateStatus(document.getId(), vehicle);
                            }
                        }
                    }
                });

            Intent intent = new Intent(this, VehicleInfoActivity.class);
            startActivity(intent);
    }

    public void bookVehicle(View v)
    {
        if(capacity.equals("0")) {Toast.makeText(getApplicationContext(), "capacity full", Toast.LENGTH_SHORT).show();}
        else {if(open.equals("Closed")) {Toast.makeText(getApplicationContext(), "booking closed", Toast.LENGTH_SHORT).show();}
            else {
                firestore = FirebaseFirestore.getInstance();
                firestore.collection("Vehicles")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                vehicle = new Vehicle();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    vehicle = document.toObject(Vehicle.class);
                                    if (id.equals(vehicle.getID())) {
                                        vehicle.decrementCapacity();
                                        updateStatus(document.getId(), vehicle);
                                        Toast.makeText(getApplicationContext(), "Vehicle booked!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                firestore.collection("User")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                user = new User();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    user = document.toObject(User.class);
                                    if (mUser.getEmail().equals(user.getEmail())) {
                                        user.addVehicleRode(vehicle);
                                        updateUserRide(document.getId(), user);
                                    }
                                }
                            }
                        });
            }
        }

        Intent intent = new Intent(this, VehicleInfoActivity.class);
        startActivity(intent);
    }

    public void updateStatus(String docID, Vehicle v) {firestore.collection("Vehicles").document(docID).set(v);}
    public void updateUserRide(String docID, User u) {firestore.collection("User").document(docID).set(u);}

}