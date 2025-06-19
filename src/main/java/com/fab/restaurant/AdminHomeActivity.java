package com.fab.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.fab.restaurant.databinding.ActivityAdminHomeBinding;

public class AdminHomeActivity extends AppCompatActivity {
    ActivityAdminHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnApproveHotel.setOnClickListener(view -> {
            startActivity(new Intent(this, ApproveHotelActivity.class));
        });
    }
}