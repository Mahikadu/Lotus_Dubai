package com.prod.sudesi.lotusherbalsnew.Models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Admin on 28-10-2017.
 */

public class ProductModel implements Serializable {

    String ProductName;
    String Barcodes;
    String A_Id;
    String PTT;
    String SingleOffer;
    String size;
    String Brand;
    String Category;
    String SubCategory;

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getBarcodes() {
        return Barcodes;
    }

    public void setBarcodes(String barcodes) {
        Barcodes = barcodes;
    }

    public String getA_Id() {
        return A_Id;
    }

    public void setA_Id(String a_Id) {
        A_Id = a_Id;
    }

    public String getPTT() {
        return PTT;
    }

    public void setPTT(String PTT) {
        this.PTT = PTT;
    }

    public String getSingleOffer() {
        return SingleOffer;
    }

    public void setSingleOffer(String singleOffer) {
        SingleOffer = singleOffer;
    }



}
