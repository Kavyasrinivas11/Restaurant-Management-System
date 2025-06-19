package com.fab.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.fab.restaurant.Adapter.CategoryAdapter;
import com.fab.restaurant.Adapter.HotelAdapter;
import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.databinding.ActivityMainBinding;
import com.fab.restaurant.model.HotelList;
import com.fab.restaurant.model.ResWithData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    List<HotelList> hotelLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getHotels();
    }
    public void getHotels() {
        JSONObject reqObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            Call<JsonObject> call = ApiClientCustom.getInterface().getHotels((JsonObject) jsonParser.parse(reqObject.toString()));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject resObj = response.body();
                    Gson gson = new Gson();
                    ResWithData resWithData = gson.fromJson(resObj.toString(), ResWithData.class);
                    hotelLists = new ArrayList<>();
                    if(resWithData.getResData().getResList() != null) {
                        hotelLists = resWithData.getResData().getResList();
                        HotelAdapter adapter = new HotelAdapter(MainActivity.this, hotelLists);
                        binding.rvHotels.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        binding.rvHotels.setAdapter(adapter);
                        binding.rvHotels.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
        catch (Exception e) {

        }
    }
}