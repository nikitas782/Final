package com.nikitastzouk.afinal;

import java.util.List;

public class Orders {
    private String customerName;
    private List<Products> products;
    private String timestamp;

    public Orders() {} // Empty constructor for Firebase

    public Orders(String customerName, List<Products> products, String timestamp) {
        this.customerName = customerName;
        this.products = products;
        this.timestamp = timestamp;
    }

    public String getCustomerName() { return customerName; }
    public List<Products> getProducts() { return products; }
    public String getTimestamp() { return timestamp; }
}







