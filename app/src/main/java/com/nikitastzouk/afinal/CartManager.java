package com.nikitastzouk.afinal;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<Products> cart = new ArrayList<>();

    public static void addToCart(Products product) {
        cart.add(product);
    }

    public static List<Products> getCart() {
        return new ArrayList<>(cart);
    }

    public static void clearCart() {
        cart.clear();
    }

}
