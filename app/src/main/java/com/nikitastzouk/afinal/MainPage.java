package com.nikitastzouk.afinal;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

public class MainPage extends AppCompatActivity implements LocationListener {


    LocationManager locationManager;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "eshop_channel",
                    "Nearby Store Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }//notification cchannel


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
            intent.putExtra("id",products.getId());
            startActivity(intent);
        });// passes the products info to productDetails page


        recyclerView.setAdapter(adapter);//we show in the recycleview the products

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
                    Double lat = productSnapshot.child("LAT").getValue(Double.class);
                    Double lng = productSnapshot.child("LNG").getValue(Double.class);
                    String id = productSnapshot.child("ID").getValue(String.class);
                    if (name != null && price != null && release_date != null && description != null && location != null && lat != null && lng != null) {
                        productsList.add(new Products(name,price,description,release_date,location,lat,lng,id));
                    }
                }
                adapter.notifyDataSetChanged();
            }//create the list of products

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to load products", error.toException());
            }


        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
            userLocation();//ask for permission to get location






    }
    private void userLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,10,this);

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double userLat = location.getLatitude();
        double userLng = location.getLongitude();
        Log.d("LocationUpdate", "New Location: Lat=" + location.getLatitude() + ", Lng=" + location.getLongitude());

        nearStores(userLat,userLng);
    }//check for nearby stores with void nearStores

    private void nearStores(double userLat, double userLng){
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DataSnapshot> nearbyProducts = new ArrayList<>();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {


                    double storeLat = productSnapshot.child("LAT").getValue(Double.class);
                    double storeLng = productSnapshot.child("LNG").getValue(Double.class);



                    float[] results = new float[1];
                    Location.distanceBetween(userLat, userLng, storeLat, storeLng, results);
                    float distanceInMeters = results[0];
                    Log.d("FirebaseData", "distannce" + distanceInMeters + " at LAT=" + storeLat + ", LNG=" + storeLng);

                    if (distanceInMeters <= 200) { // User is within 200m
                        nearbyProducts.add(productSnapshot);


                    }
                }
                sendNotificationsWithList(nearbyProducts,0);

            }//if we find products on a near store we add all of the products in a list and notify the user for every item


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Database query failed", error.toException());
            }
        });



    }
    private void sendNotificationsWithList(List<DataSnapshot> products, int index) {
        if (index >= products.size()) {
            return;  // Stop if we've sent all notifications
        }

        DataSnapshot productSnapshot = products.get(index);
        String productName = productSnapshot.child("NAME").getValue(String.class);
        String location = productSnapshot.child("LOCATION").getValue(String.class);
        Double price = productSnapshot.child("PRICE").getValue(Double.class);
        String release_date = productSnapshot.child("RELEASE_DATE").getValue(String.class);
        String description = productSnapshot.child("DESCRIPTION").getValue(String.class);

        sendNotification(productName, location,price,release_date,description,index);  // Send the notification for this product


        new CountDownTimer(0, 0) {  // Delay of 10 seconds
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                // send the next notification
                sendNotificationsWithList(products, index + 1);
            }
        }.start();    }
    private void sendNotification(String productName, String location, double price, String release_date, String description, int index) {

        Intent intent = new Intent(this, ProductsDetails.class);
        intent.putExtra("name", productName);
        intent.putExtra("price",price);
        intent.putExtra("description",description);
        intent.putExtra("release_date",release_date);
        intent.putExtra("location", location);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "eshop_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Nearby Store Alert!")
                .setContentText("You're near a store selling: " + productName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS},123);
            return;

        }

        notificationManager.notify(index, builder.build());

    }//create the notification window after asking for permission
    public void settings(View view){
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);

    }
    public void viewcart(View view){
        Intent intent = new Intent(this,CartActivity.class);
        startActivity(intent);
    }

}