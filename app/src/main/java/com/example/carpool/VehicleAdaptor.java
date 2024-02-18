package com.example.carpool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class VehicleAdaptor extends RecyclerView.Adapter<VehicleAdaptor.VehicleActivityHolder>
{
        private ArrayList<Vehicle> all;
        private RecyclerViewClickListener listener;

        public VehicleAdaptor (ArrayList<Vehicle> myVehicles, RecyclerViewClickListener listener)
        {
            all = myVehicles;
            this.listener = listener;
        }

    public class VehicleActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            protected TextView model;
            protected TextView capacity;

            public VehicleActivityHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(this); 

                model = itemView.findViewById(R.id.holderM);
                capacity = itemView.findViewById(R.id.holderC);
            }

            @Override
            public void onClick(View view) {listener.onClick(view, getAdapterPosition());}
        }
        @NonNull
        @Override
    public VehicleActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View myView = LayoutInflater.from(parent.getContext()).inflate
                    (R.layout.activity_vehicle_rec_view, parent, false);
            VehicleActivityHolder holder = new VehicleActivityHolder(myView);
            return holder;
        }

    @Override
    public void onBindViewHolder(@NonNull VehicleActivityHolder holder, int position) {
            Vehicle v = all.get(position);
            holder.model.setText("    "+v.getType()+" - "+v.getModel());
            holder.capacity.setText("Seats left: "+v.getCapacity().toString());
        }

        @Override
    public int getItemCount()
    {
        return all.size();
    }



    public interface RecyclerViewClickListener{
            void onClick(View v, int position);
    }
}
