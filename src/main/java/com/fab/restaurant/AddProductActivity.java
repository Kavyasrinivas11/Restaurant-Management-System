package com.fab.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fab.restaurant.Adapter.ProductAdapter;
import com.fab.restaurant.Network.ApiClientCustom;
import com.fab.restaurant.Network.GetResult;
import com.fab.restaurant.databinding.ActivityAddProductBinding;
import com.fab.restaurant.model.CategoryList;
import com.fab.restaurant.model.ProductList;
import com.fab.restaurant.model.ResWithData;
import com.fab.restaurant.model.ResponseCommon;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, GetResult.MyListener{
    ActivityAddProductBinding binding;
    ArrayList<CategoryList> catList;
    ArrayList<String> catStrList;
    List<CategoryList> categoryLists;
    List<ProductList> productLists;
    ImageView img_view;
    Button btnSubmit, btnChooseImg;
    EditText txtDesc;
    String eventencodedImage = "", imgname = "";
    private Bitmap bitmap;
    private File destination = null;
    private String imgPath = null;
    private final int CGALLERY = 1;
    private final int CAMERA = 2;
    SharedPreferences preferences;
    RecyclerView rv_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        img_view = findViewById(R.id.imgItem);
        btnChooseImg = findViewById(R.id.btnImgUpload);
        rv_data = binding.rvProduct;
        btnChooseImg.setOnClickListener(this);
        preferences = getSharedPreferences("myPref", MODE_PRIVATE);
        listProduct();
        binding.btnAddProduct.setOnClickListener(view -> {
            Log.d("Click", "Click");
            listCategory();
            binding.addProdPopup.setVisibility(View.VISIBLE);
        });
        binding.btnSubmit.setOnClickListener(view -> {
            addProduct();
        });
    }
    public void listProduct() {
        JSONObject reqObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            if(preferences.getString("ut","").equals("restaurant")) {
                reqObject.put("rid", preferences.getString("user", ""));
                binding.btnAddProduct.setVisibility(View.VISIBLE);
            }
            else {
                binding.btnVWCart.setVisibility(View.VISIBLE);
                reqObject.put("rid", preferences.getString("rid", ""));
            }
            Call<JsonObject> call = ApiClientCustom.getInterface().getProducts((JsonObject) jsonParser.parse(reqObject.toString()));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject resObj = response.body();
                    Gson gson = new Gson();
                    ResWithData resWithData = gson.fromJson(resObj.toString(), ResWithData.class);
                    productLists = new ArrayList<>();
                    if(resWithData.getResData().getResProdList() != null) {
                        productLists = resWithData.getResData().getResProdList();
                        ProductAdapter adapter = new ProductAdapter(AddProductActivity.this, productLists);
                        rv_data.setLayoutManager(new LinearLayoutManager(AddProductActivity.this));
                        rv_data.setAdapter(adapter);
                        rv_data.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void listCategory() {
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
                    catList = new ArrayList<>();
                    catStrList = new ArrayList<>();
                    if(resWithData.getResData().getResCatList() != null) {
                        categoryLists = resWithData.getResData().getResCatList();
                        int i = 1;
                        for (CategoryList cat : categoryLists) {
                            catList.add(new CategoryList(String.valueOf(cat.getId()), cat.getRid(), cat.getCategory()));
                            catStrList.add(cat.getCategory());
                            i = i+ 1;
                        }
                        ArrayAdapter<String> adapter =new ArrayAdapter<String>(AddProductActivity.this, R.layout.spinner_text, catStrList);
                        binding.spnCategory.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addProduct() {

        JSONObject rObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        try {
            rObject.put("rid", preferences.getString("user", ""));
            rObject.put("image", eventencodedImage);
            rObject.put("name", binding.txtProdName.getText().toString().trim());
            rObject.put("type", binding.spnFtype.getSelectedItem().toString().trim());
            rObject.put("cat", binding.spnCategory.getSelectedItem().toString().trim());
            rObject.put("qty", binding.txtQty.getText().toString().trim());
            rObject.put("price", binding.txtPrice.getText().toString().trim());
            rObject.put("imgname", String.valueOf(Calendar.getInstance().getTimeInMillis()));
            Call<JsonObject> call = ApiClientCustom.getInterface().addProduct((JsonObject) jsonParser.parse(rObject.toString()));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject object = response.body();
                    Gson gson = new Gson();
                    ResponseCommon res =gson.fromJson(object.toString(), ResponseCommon.class);
                    Toast.makeText(AddProductActivity.this, res.getResponseMsg().toString(), Toast.LENGTH_SHORT);
                    if(res.getResponseCode().equals("200")) {
                        binding.txtProdName.setText("");
                        binding.txtQty.setText("");
                        binding.txtPrice.setText("");
                        startActivity(new Intent(AddProductActivity.this, RestaurantHomeActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==  this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == CGALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 5, byteArrayOutputStream);
                    img_view.setVisibility(View.VISIBLE);
                    img_view.setImageBitmap(bitmap);
                    eventencodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                    imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {


            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, bytes);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                eventencodedImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                imgPath = destination.getAbsolutePath();
                img_view.setVisibility(View.VISIBLE);
                img_view.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {


        }


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnImgUpload:
                selectImage();
                break;
        }
    }

    private void selectImage() {
        try {
            PackageManager pm = this.getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, this.getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, CGALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {

    }
}