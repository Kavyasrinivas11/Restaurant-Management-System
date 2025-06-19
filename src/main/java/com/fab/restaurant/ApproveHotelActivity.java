package com.fab.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.fab.restaurant.Adapter.UnapprovedAdapter;
import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.model.ResWithData;
import com.fab.restaurant.model.UnapprovedList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveHotelActivity extends AppCompatActivity {
    RecyclerView rv_data;
    List<UnapprovedList> unapprovedLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_hotel);
        JsonObject jsonObject = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        rv_data = findViewById(R.id.rvUnapprovedList);
        Call<JsonObject> call = ApiClientCustom.getInterface().getUnapprovedHotels((JsonObject) jsonParser.parse(jsonObject.toString()));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject object = response.body();
                Gson gson = new Gson();
                ResWithData resWithData = gson.fromJson(object.toString(), ResWithData.class);
                unapprovedLists = new ArrayList<>();
                if(resWithData.getResData().getUnapprovedLists() != null) {
                    unapprovedLists = resWithData.getResData().getUnapprovedLists();
                    UnapprovedAdapter adapter =new UnapprovedAdapter(ApproveHotelActivity.this, unapprovedLists);
                    rv_data.setLayoutManager(new LinearLayoutManager(ApproveHotelActivity.this));
                    rv_data.setAdapter(adapter);
                    rv_data.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}