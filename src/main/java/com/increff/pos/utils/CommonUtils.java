package com.increff.pos.utils;

import com.increff.pos.model.UploadErrorMessage;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;

public class CommonUtils {

    public UploadErrorMessage setError(int row, String message){
        UploadErrorMessage uploadErrorMessage = new UploadErrorMessage();
        uploadErrorMessage.setRowNumber(row);
        uploadErrorMessage.setError(message);
        return uploadErrorMessage;
    }
    public void normalize(BrandPojo brandPojo) {
        brandPojo.setBrand(StringUtil.toLowerCase(brandPojo.getBrand()));
        brandPojo.setCategory(StringUtil.toLowerCase(brandPojo.getCategory()));
    }
    public void normalize(BrandForm brandForm) {
        brandForm.setBrand(StringUtil.toLowerCase(brandForm.getBrand()));
        brandForm.setCategory(StringUtil.toLowerCase(brandForm.getCategory()));
    }
    public void normalize(ProductPojo productPojo) {
        productPojo.setName(StringUtil.toLowerCase(productPojo.getName()));
        productPojo.setBarcode(StringUtil.toLowerCase(productPojo.getBarcode()));
    }
    public void normalize(ProductForm productForm){
        productForm.setBrand(StringUtil.toLowerCase((productForm.getBrand())));
        productForm.setCategory(StringUtil.toLowerCase((productForm.getCategory())));
        productForm.setBarcode(StringUtil.toLowerCase((productForm.getBarcode())));
        productForm.setName(StringUtil.toLowerCase((productForm.getName())));
    }
    public void normalize(InventoryForm inventoryForm){
        inventoryForm.setBarcode(StringUtil.toLowerCase((inventoryForm.getBarcode())));
    }

}
