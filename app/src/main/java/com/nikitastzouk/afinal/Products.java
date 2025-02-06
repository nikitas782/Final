package com.nikitastzouk.afinal;

public class Products {
    private String name;
    private String description;
    private String release_date;
    private String location;
    private double price;
    private String id;



    private Double lat;
    private Double lng;

    // Required empty constructor for Firebase
    public Products() {}

    public Products(String name, double price, String description, String release_date, String location, Double lat, Double lng, String id ) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.release_date = release_date;
        this.location = location;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
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
    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getId(){return id;}
}
