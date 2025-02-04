package com.nikitastzouk.afinal;

public class Products {
    private String name;
    private String description;
    private String release_date;
    private String location;
    private double price;

    // Required empty constructor for Firebase
    public Products() {}

    public Products(String name, double price, String description, String release_date, String location ) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.release_date = release_date;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getLocation() {
        return location;
    }
}
