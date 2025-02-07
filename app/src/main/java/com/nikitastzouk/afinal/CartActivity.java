package com.nikitastzouk.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private Button placeOrderButton;
    List<Products> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        recyclerView = findViewById(R.id.recyclerView);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = CartManager.getCart();
        adapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(adapter);



    }

    //create the order for the selected products and the connected user
    public void placeOrder(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userID = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("accounts").child(userID);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fullName = snapshot.child("username").getValue(String.class);
                        saveOrderToFirebase(fullName);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Failed to fetch user data", error.toException());
                }
            });
        }
    }

    //save order details to our firebase
    private void saveOrderToFirebase(String customerName) {
       if ( adapter.getItemCount() != 0){
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = ordersRef.push().getKey();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Orders order = new Orders(customerName, CartManager.getCart(), timestamp);
        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(aVoid -> Log.d("Order", "Order placed successfully!"))
                .addOnFailureListener(e -> Log.e("Order", "Failed to place order", e));
        Toast.makeText(getApplicationContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
        adapter.clearAdapter();
        recyclerView.setAdapter(null);
        CartManager.clearCart();
       }else {
           Toast.makeText(getApplicationContext(), "Empty cart", Toast.LENGTH_SHORT).show();

       }

}}
