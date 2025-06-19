package com.fab.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.databinding.ActivityLoginBinding;
import com.fab.restaurant.model.ResponseCommon;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnLgn.setOnClickListener(view -> {
            if (binding.lgnEmail.getText().toString().trim().equals("") ||
                    binding.lgnPassword.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please provide credentials", Toast.LENGTH_SHORT).show();
            } else {
                DoLogin();
            }
        });
        binding.btnSignup.setOnClickListener(view -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }
    private void DoLogin() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            jsonObject.put("email", binding.lgnEmail.getText().toString().trim());
            jsonObject.put("password", binding.lgnPassword.getText().toString().trim());
            Call<JsonObject> call = ApiClientCustom.getInterface().login((JsonObject) jsonParser.parse(jsonObject.toString()));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject object = response.body();
                    Gson gson = new Gson();
                    ResponseCommon responseCommon = gson.fromJson(object.toString(), ResponseCommon.class);
                    String resCode = (String) responseCommon.getResponseCode();
                    if(Integer.parseInt(resCode) == 200) {
                        String userType = responseCommon.getAddMsg();
                        SharedPreferences preferences = getSharedPreferences("myPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = preferences.edit();
                        myEdit.putString("user", responseCommon.getResponseMsg());
                        myEdit.putString("ut", responseCommon.getAddMsg());
                        myEdit.apply();
                        if(userType.equals("admin")) {
                            GoToAdminHome();
                        }
                        else if(userType.equals("restaurant")) {
                            GoToRestaurantHome();
                        }
                        else {
                            GoToMainActivity();
                        }
                    }
                    else {
                        Log.d("Result_text", resCode);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void GoToRestaurantHome() {
        startActivity(new Intent(this, RestaurantHomeActivity.class));
        finish();
    }
    private void GoToAdminHome() {
        startActivity(new Intent(this, AdminHomeActivity.class));
    }
    private void GoToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}