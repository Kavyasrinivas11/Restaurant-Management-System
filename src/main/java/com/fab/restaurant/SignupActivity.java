package com.fab.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.databinding.ActivitySignupBinding;
import com.fab.restaurant.model.ResponseCommon;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivitySignupBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);
        binding.btnCreateUser.setOnClickListener(view1 -> {
            DoSignUp();
        });
    }
    private void DoSignUp() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            jsonObject.put("email", binding.signupEmail.getText().toString().trim());
            jsonObject.put("password", binding.signupPswd.getText().toString().trim());
            jsonObject.put("name", binding.signupName.getText().toString().trim());
            jsonObject.put("contact", binding.signupContact.getText().toString().trim());
            jsonObject.put("type", binding.signupUtype.getSelectedItem().toString().trim());
            Call<JsonObject> call = ApiClientCustom.getInterface().signup((JsonObject) jsonParser.parse(jsonObject.toString()));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject object = response.body();
                    Gson gson = new Gson();
                    ResponseCommon responseCommon = gson.fromJson(object.toString(), ResponseCommon.class);
                    String msg = responseCommon.getResponseMsg().toString();
                    Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }catch (Exception e) {
            //Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}