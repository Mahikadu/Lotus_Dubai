package com.prod.sudesi.lotusherbalsdubai.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 31-10-2017.
 */

public class ProductListModel extends ProductModel implements Serializable {

    ArrayList<ProductModel> productListModels;

    public ArrayList<ProductModel> getProductListModels() {
        return productListModels;
    }

    public void setProductListModels(ArrayList<ProductModel> productListModels) {
        this.productListModels = productListModels;
    }

}
