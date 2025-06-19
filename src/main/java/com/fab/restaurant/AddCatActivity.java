package com.fab.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fab.restaurant.Adapter.CategoryAdapter;
import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.databinding.ActivityAddCatBinding;
import com.fab.restaurant.model.CategoryList;
import com.fab.restaurant.model.ResWithData;
import com.fab.restaurant.model.ResponseCommon;
import com.fab.restaurant.model.UnapprovedList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCatActivity extends AppCompatActivity {
    ActivityAddCatBinding binding;
    RecyclerView rv_data;
    List<CategoryList> categoryLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCatBinding.inflate(getLayoutInflater());
        rv_data = binding.rvCategory;
        setContentView(binding.getRoot());
        SharedPreferences preferences = getSharedPreferences("myPref", MODE_PRIVATE);
        JSONObject reqObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            reqObject.put("rid", preferences.getString("user",""));
            Call<JsonObject> call = ApiClientCustom.getInterface().getCategory((JsonObject) jsonParser.parse(reqObject.toString()));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject resObj = response.body();
                    Gson gson = new Gson();
                    ResWithData resWithData = gson.fromJson(resObj.toString(), ResWithData.class);
                    categoryLists = new ArrayList<>();
                    if(resWithData.getResData().getResCatList() != null) {
                        categoryLists = resWithData.getResData().getResCatList();
                        CategoryAdapter adapter = new CategoryAdapter(AddCatActivity.this, categoryLists);
                        rv_data.setLayoutManager(new LinearLayoutManager(AddCatActivity.this));
                        rv_data.setAdapter(adapter);
                        rv_data.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.btnAddCat.setOnClickListener(view -> {
            binding.addCatPopup.setVisibility(View.VISIBLE);
        });
        binding.btnCataddSubmit.setOnClickListener(view -> {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("rid", preferences.getString("user",""));
                jsonObject.put("name", binding.txtCategory.getText().toString().trim());
                Call<JsonObject> call = ApiClientCustom.getInterface().addCategory((JsonObject) jsonParser.parse(jsonObject.toString()));
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject object = response.body();
                        Gson gson = new Gson();
                        ResponseCommon responseCommon = gson.fromJson(object.toString(), ResponseCommon.class);
                        Toast.makeText(AddCatActivity.this, responseCommon.getResponseMsg(), Toast.LENGTH_SHORT).show();
                        binding.addCatPopup.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}