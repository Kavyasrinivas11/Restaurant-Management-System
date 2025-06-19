package com.fab.restaurant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultData {
    @SerializedName("unapprovedList")
    @Expose
    private List<UnapprovedList> unapprovedLists =null;

    @SerializedName("resProdList")
    @Expose
    private List<ProductList> resProdList =null;

    @SerializedName("resList")
    @Expose
    private List<HotelList> resList =null;

    @SerializedName("resCatList")
    @Expose
    private List<CategoryList> resCatList =null;

    public List<HotelList> getResList() {
        return resList;
    }

    public void setResList(List<HotelList> resList) {
        this.resList = resList;
    }

    public List<UnapprovedList> getUnapprovedLists() {
        return unapprovedLists;
    }

    public void setUnapprovedLists(List<UnapprovedList> unapprovedLists) {
        this.unapprovedLists = unapprovedLists;
    }

    public List<CategoryList> getResCatList() {
        return resCatList;
    }

    public void setResCatList(List<CategoryList> resCatList) {
        this.resCatList = resCatList;
    }


    public List<ProductList> getResProdList() {
        return resProdList;
    }

    public void setResProdList(List<ProductList> resProdList) {
        this.resProdList = resProdList;
    }
}
