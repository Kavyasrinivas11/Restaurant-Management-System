package com.fab.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.fab.restaurant.databinding.ActivityRestaurantHomeBinding;

public class RestaurantHomeActivity extends AppCompatActivity {
    ActivityRestaurantHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnManageCat.setOnClickListener(view -> {
            startActivity(new Intent(this, AddCatActivity.class));
        });
        binding.btnManageProd.setOnClickListener(view -> {
            startActivity(new Intent(this, AddProductActivity.class));
        });
    }
}