package com.nikitastzouk.afinal;

public class Products {
    private String name;
    private String description;
    private String release_date;
    private String location;
    private double price;



    private double lat;
    private double lng;

    // Required empty constructor for Firebase
    public Products() {}

    public Products(String name, double price, String description, String release_date, String location, double lat, double lng ) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.release_date = release_date;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
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
    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
