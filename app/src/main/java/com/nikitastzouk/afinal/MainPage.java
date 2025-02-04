package com.nikitastzouk.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Products> productsList = new ArrayList<>();
        ProductAdapter adapter = new ProductAdapter(productsList, products -> {
            //detailed page
            Intent intent = new Intent(this, ProductsDetails.class);
            intent.putExtra("name", products.getName());
            intent.putExtra("price", products.getPrice());
            intent.putExtra("description",products.getDescription());
            intent.putExtra("release_date",products.getRelease_date());
            intent.putExtra("location",products.getLocation());
            startActivity(intent);
        });


        recyclerView.setAdapter(adapter);

        // Initialize Firebase and fetch data
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productsList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String name = productSnapshot.child("NAME").getValue(String.class);
                    Double price = productSnapshot.child("PRICE").getValue(Double.class);
                    String release_date = productSnapshot.child("RELEASE_DATE").getValue(String.class);
                    String description = productSnapshot.child("DESCRIPTION").getValue(String.class);
                    String location = productSnapshot.child("LOCATION").getValue(String.class);
                    if (name != null && price != null && release_date != null && description != null && location != null) {
                        productsList.add(new Products(name,price,description,release_date,location));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to load products", error.toException());
            }
        });


    }
}