package com.fab.restaurant.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fab.restaurant.AdminHomeActivity;
import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.RestaurantHomeActivity;
import com.fab.restaurant.databinding.ResCatListBinding;
import com.fab.restaurant.model.CategoryList;
import com.fab.restaurant.model.ResponseCommon;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    List<CategoryList> lists;

    public CategoryAdapter(Context context, List<CategoryList> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ResCatListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryList list = lists.get(position);
        holder.binding.txtCatName.setText(list.getCategory());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ResCatListBinding binding;
        public ViewHolder(ResCatListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btnDeleteResCat.setOnClickListener(view -> {
                JSONObject jsonObject = new JSONObject();
                JsonParser jsonParser = new JsonParser();
                String cid = lists.get(getAdapterPosition()).getId().toString();
                try {
                    jsonObject.put("cid", cid);
                    Call<JsonObject> call = ApiClientCustom.getInterface().deleteCategory((JsonObject) jsonParser.parse(jsonObject.toString()));
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            JsonObject object = response.body();
                            Gson gson = new Gson();
                            ResponseCommon responseCommon = gson.fromJson(object.toString(), ResponseCommon.class);
                            Toast.makeText(context, responseCommon.getResponseMsg(), Toast.LENGTH_SHORT).show();
                            if(responseCommon.getResponseCode().equals("200")) {
                                context.startActivity(new Intent(context, RestaurantHomeActivity.class));
                            }
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
}
