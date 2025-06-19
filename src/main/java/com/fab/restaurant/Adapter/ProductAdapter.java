package com.fab.restaurant.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.databinding.ResProdItemBinding;
import com.fab.restaurant.model.ProductList;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    List<ProductList> lists;
    SharedPreferences pref;
    String rid;
    public ProductAdapter(Context context, List<ProductList> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ResProdItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductList list = lists.get(position);
        holder.binding.rpiProdName.setText(list.getName());
        holder.binding.rpiProdCat.setText(list.getCategory() + " (" + list.getType() + " )");
        holder.binding.rpiProdPrice.setText(list.getPrice() + " / " + list.getQty() + " (Quatity)");
        Glide.with(context)
                .load(ApiClientCustom.baseUrl + "restaurant_managment/uploads/" + list.getImage() + ".jpg")
                .into(holder.binding.rpiImage);
        if(pref.getString("ut", "").equals("public")) {
            holder.binding.prodAdd.setVisibility(View.VISIBLE);
        }
        holder.binding.btnAdd.setOnClickListener(view -> {
            int qty = Integer.parseInt(holder.binding.txtQty.getText().toString()) + 1;
            holder.binding.txtQty.setText(String.valueOf(qty));
        });
        holder.binding.btnMinus.setOnClickListener(view -> {
            int qty = Integer.parseInt( holder.binding.txtQty.getText().toString()) - 1;
            if(qty >= 0) {
                holder.binding.txtQty.setText(String.valueOf(qty));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ResProdItemBinding binding;
        public ViewHolder(ResProdItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            pref = context.getSharedPreferences("myPref", context.MODE_PRIVATE);
            if(pref.getString("ut", "").equals("restaurant")) {
                rid = pref.getString("user", "");
            }
            else {
                rid = pref.getString("rid", "");
            }
        }
    }
}
