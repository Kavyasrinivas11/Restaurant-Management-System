package com.fab.restaurant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryList {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("restaurant_id")
    @Expose
    private String rid;

    @SerializedName("category")
    @Expose
    private String category;

    public CategoryList(String id, String rid, String category) {
        this.id = id;
        this.rid = rid;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
