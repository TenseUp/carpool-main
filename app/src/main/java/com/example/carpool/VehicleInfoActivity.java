package com.example.carpool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Random;


public class VehicleInfoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private RecyclerView recycler;
    private ArrayList<Vehicle> vehicles;
    private VehicleAdaptor.RecyclerViewClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        recycler = findViewById(R.id.recyclerView);

        paste();
        Toast.makeText(getApplicationContext(), "Choose an EV! Help out by saving emissions.", Toast.LENGTH_SHORT).show();
    }


    public void paste(){

        firestore.collection("Vehicles")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        vehicles = new ArrayList<Vehicle>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            vehicles.add(document.toObject(Vehicle.class));
                        }

                        setOnClickListener(); 
                        VehicleAdaptor myAdaptor = new VehicleAdaptor(vehicles, listener);
                        recycler.setAdapter(myAdaptor);
                        recycler.setLayoutManager(new LinearLayoutManager
                                (VehicleInfoActivity.this));
                    }
                });
    }

    private void setOnClickListener()
    {
        listener = (v, position) -> {
            Intent i = new Intent(getApplicationContext(), VehicleProfileActivity.class);
            i.putExtra("model", vehicles.get(position).getModel());
            i.putExtra("capacity", vehicles.get(position).getCapacity().toString());
            i.putExtra("owner", vehicles.get(position).getEmail());
            i.putExtra("type", vehicles.get(position).getType());
            i.putExtra("openStatus", vehicles.get(position).getOpen());
            i.putExtra("vID", vehicles.get(position).getID());
            startActivity(i);
            finish();
        };
    }

    public void gotoaddvehicles(View v)
    {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    public void refresh (View v){
        startActivity(new Intent(this, VehicleInfoActivity.class));
        finish();
    }

    public void randomRide(View v) {
        int vehicleCount = vehicles.size();
        if (vehicleCount > 0) {
            Random random = new Random();
            int randomIndex = random.nextInt(vehicleCount);

            listener.onClick(null, randomIndex);
        }
        else{
            Toast.makeText(getApplicationContext(), "There are no vehicles to choose from!", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoprofile(View v)
    {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}