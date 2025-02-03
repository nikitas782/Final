package com.nikitastzouk.afinal;

public class Accounts {

    private String email;
    private String username;

    // Constructor
    public Accounts(String username,String email) {
        this.username = username;
        this.email = email;
        // Default constructor required for Firebase Realtime Database
    }

    // Getters and Setters


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
