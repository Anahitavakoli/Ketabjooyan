package com.anahitavakoli.ketabjooyan.info;

import com.anahitavakoli.ketabjooyan.model.Products;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductInfo {

    public List<Products> getProductInfo(){


        List<Products> ls = new ArrayList<>();

        Products[] prArray = {new Products()};
        ls = Arrays.asList(prArray);
        return ls;
    }
}
