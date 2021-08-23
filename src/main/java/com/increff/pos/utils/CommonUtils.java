package com.increff.pos.utils;

import com.increff.pos.model.UploadErrorMessage;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;

public final class CommonUtils {

    public static void normalize(BrandPojo brandPojo) {
        brandPojo.setBrand(StringUtil.toLowerCase(brandPojo.getBrand()).trim());
        brandPojo.setCategory(StringUtil.toLowerCase(brandPojo.getCategory()).trim());
    }
    public static void normalize(BrandForm brandForm) {
        brandForm.setBrand(StringUtil.toLowerCase(brandForm.getBrand()).trim());
        brandForm.setCategory(StringUtil.toLowerCase(brandForm.getCategory()).trim());
    }
    public static void normalize(ProductPojo productPojo) {
        productPojo.setName(StringUtil.toLowerCase(productPojo.getName()).trim());
        productPojo.setBarcode(StringUtil.toLowerCase(productPojo.getBarcode()).trim());
    }
    public static void normalize(ProductForm productForm){
        productForm.setBrand(StringUtil.toLowerCase((productForm.getBrand())).trim());
        productForm.setCategory(StringUtil.toLowerCase((productForm.getCategory())).trim());
        productForm.setBarcode(StringUtil.toLowerCase((productForm.getBarcode())).trim());
        productForm.setName(StringUtil.toLowerCase((productForm.getName())).trim());
    }
    public static void normalize(InventoryForm inventoryForm){
        inventoryForm.setBarcode(StringUtil.toLowerCase((inventoryForm.getBarcode())).trim());
    }

}
