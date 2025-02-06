package com.nikitastzouk.afinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProductsDetails extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView priceTextView = findViewById(R.id.priceTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView releasedateTextView = findViewById(R.id.releasedateTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);

        String name = getIntent().getStringExtra("name");
        double price = getIntent().getDoubleExtra("price",0.0);
        String description = getIntent().getStringExtra("description");
        String releasedate = getIntent().getStringExtra("release_date");
        String location = getIntent().getStringExtra("location");
        String id = getIntent().getStringExtra("id");

        nameTextView.setText(name);
        priceTextView.setText("$" + price);
        descriptionTextView.setText(description);
        releasedateTextView.setText(releasedate);
        locationTextView.setText(location);



    }

    public void buy (View view){
        String name = getIntent().getStringExtra("name");
        String id= getIntent().getStringExtra("id");
        double price = getIntent().getDoubleExtra("price", 0.0);

        if (name != null && id != null) {
            Products product = new Products(name, price,null,null,null,0.0,0.0,id);
            CartManager.addToCart(product);

            // Navigate to cart activity
            Intent intent = new Intent(ProductsDetails.this, MainPage.class);
            startActivity(intent);
    }
}}