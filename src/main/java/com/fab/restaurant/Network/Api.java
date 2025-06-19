package com.fab.restaurant.Network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {
    @POST(ApiClientCustom.APPEND_URL + "login")
    Call<JsonObject> login(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "signup")
    Call<JsonObject> signup(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "unapprovedlist")
    Call<JsonObject> getUnapprovedHotels(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "approvehotel")
    Call<JsonObject> approvedHotel(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "rejecthotel")
    Call<JsonObject> rejectHotel(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "addcat")
    Call<JsonObject> addCategory(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "catlist")
    Call<JsonObject> getCategory(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "deletecat")
    Call<JsonObject> deleteCategory(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "addproduct")
    Call<JsonObject> addProduct(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "getproduct")
    Call<JsonObject> getProducts(@Body JsonObject object);

    @POST(ApiClientCustom.APPEND_URL + "gethotels")
    Call<JsonObject> getHotels(@Body JsonObject object);
}
