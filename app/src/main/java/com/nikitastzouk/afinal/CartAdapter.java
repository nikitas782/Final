package com.nikitastzouk.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Products> cartItems;

    public CartAdapter(List<Products> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Products product = cartItems.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText("$" + product.getPrice());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void clearAdapter(){cartItems.clear();}

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(android.R.id.text1);
            priceTextView = itemView.findViewById(android.R.id.text2);
        }
    }
}
