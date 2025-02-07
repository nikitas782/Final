package com.nikitastzouk.afinal;
import static android.content.ContentValues.TAG;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textView2;
    EditText emailText,passwordText;
    Button button,button2;
    FirebaseAuth auth;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        boolean nightMODE = sharedPreferences.getBoolean("night", false);
        if (nightMODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailText = findViewById(R.id.editTextEmail);
        passwordText = findViewById(R.id.editTextTextPassword);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        textView2 = findViewById(R.id.textView2);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        int saveFontsize = sharedPreferences.getInt("font_size",20);
        emailText.setTextSize(saveFontsize);
        passwordText.setTextSize(saveFontsize);
        button.setTextSize(saveFontsize);
        button2.setTextSize(saveFontsize);
        textView2.setTextSize(saveFontsize);



    }



    public void Login (View view){


        if (!emailText.getText().toString().isEmpty() && !passwordText.getText().toString().isEmpty()){
            auth.signInWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString()) //authentication realtimedb with email,password that is given
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                user = auth.getCurrentUser();
                                String userID = user.getUid();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("accounts").child(userID); // get account from db with Uid from authentication

                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            String username = snapshot.child("username").getValue(String.class);
                                            SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("username",username);
                                            editor.apply();//save preferences for the username


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("FirebaseError", "Failed to fetch user data", error.toException()); //error if wrong inputs
                                    }
                                });
                                startActivity(new Intent(MainActivity.this,MainPage.class));

                            }else {
                                showMessage("Error",task.getException().getLocalizedMessage());
                            }
                        }
                    });
        }else {
            showMessage("Error","Please provide data to the fields");//error if no data provided
        }

        //auth.signOut();

    }

    public void signup(View view){
        startActivity(new Intent(MainActivity.this,SignUpPage.class));
    }
    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
    // Function to fetch user data from Realtime Database

    public void greek(View view){
        Locale locale = new Locale("el");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
        this.recreate();

    }
    public void spanish(View view){
        Locale locale = new Locale("es");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
        this.recreate();

    }
    public void english(View view){
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, null);
        this.recreate();

    }

}