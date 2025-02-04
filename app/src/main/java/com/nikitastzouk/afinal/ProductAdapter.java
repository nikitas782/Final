package com.nikitastzouk.afinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Products> productsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Products products);
    }
    public ProductAdapter(List<Products> productsList, OnItemClickListener listener){
        this.productsList = productsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Products products = productsList.get(position);
        holder.nameTextView.setText(products.getName());
        holder.priceTextView.setText("$" + products.getPrice());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(products));
    }
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(android.R.id.text1);
            priceTextView = itemView.findViewById(android.R.id.text2);
        }
    }
}
