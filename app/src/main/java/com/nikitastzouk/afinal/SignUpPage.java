package com.nikitastzouk.afinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {
    EditText emailText,passwordText,usernameText,firstnameText,lastnameText;

    Button button3;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailText = findViewById(R.id.editTextTextEmailAddress);
        passwordText = findViewById(R.id.editTextTextPassword2);
        usernameText = findViewById(R.id.editTextText);
        firstnameText = findViewById(R.id.editTextText2);
        lastnameText = findViewById(R.id.editTextText3);
        button3 = findViewById(R.id.button3);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("accounts");
        sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        int saveFontsize = sharedPreferences.getInt("font_size",20);
        emailText.setTextSize(saveFontsize);
        passwordText.setTextSize(saveFontsize);
        usernameText.setTextSize(saveFontsize);
        firstnameText.setTextSize(saveFontsize);
        lastnameText.setTextSize(saveFontsize);
        button3.setTextSize(saveFontsize);


    }

    public void signup(View view){
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String username = usernameText.getText().toString();
        if (!email.isEmpty() && !password.isEmpty() && !username.isEmpty()){
            auth.createUserWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                user = auth.getCurrentUser();
                                if (user != null) {
                                    String uid = user.getUid();
                                    saveUserToDatabase(uid, username, email);
                                }
                                startActivity(new Intent(SignUpPage.this,MainPage.class));
                            }else {
                                showMessage("Error",task.getException().getLocalizedMessage());
                            }
                        }
                    });
        }else {
            showMessage("Error","Please provide data to the fields");
        }

    }
    private void saveUserToDatabase(String uid, String username, String email) {
        Accounts newAcc = new Accounts(username, email);
        databaseReference.child(uid).setValue(newAcc)
                .addOnSuccessListener(aVoid -> {
                    // Δεδομένα αποθηκεύτηκαν επιτυχώς
                })
                .addOnFailureListener(e -> {
                    showMessage("Error", "Failed to save user data: " + e.getMessage());
                });
    }
    void showMessage(String title, String message){
        new AlertDialog.Builder(this).setTitle(title).setMessage(message).setCancelable(true).show();
    }
}