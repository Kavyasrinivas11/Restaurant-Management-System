package com.fab.restaurant.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fab.restaurant.AdminHomeActivity;
import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.databinding.UnapprovedListItemBinding;
import com.fab.restaurant.model.ResponseCommon;
import com.fab.restaurant.model.UnapprovedList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnapprovedAdapter extends RecyclerView.Adapter<UnapprovedAdapter.ViewHolder> {
    Context context;
    List<UnapprovedList> lists;

    public UnapprovedAdapter(Context context, List<UnapprovedList> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public UnapprovedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(UnapprovedListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnapprovedAdapter.ViewHolder holder, int position) {
        UnapprovedList list = lists.get(position);
        Log.d("List_Size", list.getName());
        holder.binding.txtHltName.setText(list.getName());
        holder.binding.txtHltEmail.setText(list.getEmail());
        holder.binding.txtHltContact.setText(list.getContact());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        UnapprovedListItemBinding binding;
        public ViewHolder(UnapprovedListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btnApprove.setOnClickListener(view -> {
                JSONObject jsonObject = new JSONObject();
                JsonParser jsonParser = new JsonParser();
                String rid = lists.get(getAdapterPosition()).getId().toString();
                try {
                    jsonObject.put("rid", rid);
                    Call<JsonObject> call = ApiClientCustom.getInterface().approvedHotel((JsonObject) jsonParser.parse(jsonObject.toString()));
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            JsonObject object = response.body();
                            Gson gson = new Gson();
                            ResponseCommon responseCommon = gson.fromJson(object.toString(), ResponseCommon.class);
                            Toast.makeText(context, responseCommon.getResponseMsg(), Toast.LENGTH_SHORT).show();
                            if(responseCommon.getResponseCode().equals("200")) {
                                context.startActivity(new Intent(context, AdminHomeActivity.class));
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
            binding.btnReject.setOnClickListener(view -> {
                JSONObject jsonObject = new JSONObject();
                JsonParser jsonParser = new JsonParser();
                String rid = lists.get(getAdapterPosition()).getId().toString();
                try {
                    jsonObject.put("rid", rid);
                    Call<JsonObject> call = ApiClientCustom.getInterface().rejectHotel((JsonObject) jsonParser.parse(jsonObject.toString()));
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            JsonObject object = response.body();
                            Gson gson = new Gson();
                            ResponseCommon responseCommon = gson.fromJson(object.toString(), ResponseCommon.class);
                            Toast.makeText(context, responseCommon.getResponseMsg(), Toast.LENGTH_SHORT).show();
                            if(responseCommon.getResponseCode().equals("200")) {
                                context.startActivity(new Intent(context, AdminHomeActivity.class));
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
