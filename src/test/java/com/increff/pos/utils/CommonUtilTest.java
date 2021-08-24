package com.increff.pos.utils;

import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonUtilTest {

    @Test
    public void testNormalizeBrandPojo(){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("nEw ");
        brandPojo.setCategory(" cAt ");
        CommonUtils.normalize(brandPojo);
        assertEquals(brandPojo.getBrand(),"new");
        assertEquals(brandPojo.getCategory(),"cat");
    }

    @Test
    public void testNormalizeBrandForm(){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("nEw ");
        brandForm.setCategory(" cAt ");
        CommonUtils.normalize(brandForm);
        assertEquals(brandForm.getBrand(),"new");
        assertEquals(brandForm.getCategory(),"cat");
    }

    @Test
    public void testNormalizeProductPojo(){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(" nAmE ");
        productPojo.setBarcode("1M2M4");
        CommonUtils.normalize(productPojo);
        assertEquals(productPojo.getBarcode(),"1m2m4");
        assertEquals(productPojo.getName(),"name");
    }

    @Test
    public void testNormalizeProductForm(){
        ProductForm productForm = new ProductForm();
        productForm.setName(" nAmE ");
        productForm.setBarcode("1M2M4");
        productForm.setBrand("nEw");
        productForm.setCategory("CAT");
        CommonUtils.normalize(productForm);
        assertEquals(productForm.getBarcode(),"1m2m4");
        assertEquals(productForm.getName(),"name");
        assertEquals(productForm.getBrand(),"new");
        assertEquals(productForm.getCategory(),"cat");
    }

    @Test
    public void testNormalizeInventoryForm(){
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(" 1We32  ");
        CommonUtils.normalize(inventoryForm);
        assertEquals(inventoryForm.getBarcode(),"1we32");
    }
}
