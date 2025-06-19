package com.fab.restaurant.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fab.restaurant.AddProductActivity;
import com.fab.restaurant.ListProductActivity;
import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.databinding.HotelListBinding;
import com.fab.restaurant.databinding.ResProdItemBinding;
import com.fab.restaurant.model.HotelList;
import com.fab.restaurant.model.ProductList;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {
    Context context;
    List<HotelList> lists;
    SharedPreferences pref;
    public HotelAdapter(Context context, List<HotelList> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public HotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(HotelListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.ViewHolder holder, int position) {
        HotelList list = lists.get(position);
        holder.binding.resName.setText(list.getName());
        /*Glide.with(context)
                .load(ApiClientCustom.baseUrl + "restaurant_managment/uploads/" + list.getImage() + ".jpg")
                .into(holder.binding.rpiImage);*/
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        HotelListBinding binding;
        public ViewHolder(HotelListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            pref = context.getSharedPreferences("myPref", context.MODE_PRIVATE);
            SharedPreferences.Editor myEdit = pref.edit();
            binding.btnSelect.setOnClickListener(view -> {
                myEdit.putString("rid", lists.get(getAdapterPosition()).getId().toString());
                myEdit.apply();
                context.startActivity(new Intent(context, AddProductActivity.class));
            });
        }
    }
}
