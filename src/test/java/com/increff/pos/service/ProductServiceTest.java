package com.increff.pos.service;


import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Test
    public void testAdd(){
        ProductPojo productPojo = null;
        try{
            BrandPojo brandPojo = create("new","cat");
            productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
            productService.add(productPojo);
            productPojo = create("1e3r4",brandPojo.getId(),"name2",123.0);
            productService.add(productPojo);

        } catch (ApiException apiException) {
            assertEquals("Barcode exists",apiException.getMessage());
        }
    }

    @Test
    public void testGetByBarcode() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        productService.add(productPojo);
        ProductPojo productPojo1 = productService.getByBarcode("1e3r4");
        assertEquals(productPojo1,productPojo);
    }

    @Test
    public void testGetById() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        productService.add(productPojo);
        ProductPojo productPojo1 = productService.getById(productPojo.getId());
        assertEquals(productPojo1.getBarcode(),productPojo.getBarcode());
    }

    @Test
    public void testGetAll() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        productService.add(productPojo);
        List<ProductPojo> productPojoList = productService.getAll();
        assertEquals(productPojoList.get(0),productPojo);
    }


    @Test
    public void testGetByBrandId() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        productService.add(productPojo);
        List<ProductPojo> productPojoList = productService.getByBrandId(brandPojo.getId());
        assertEquals(productPojoList.get(0),productPojo);
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        productService.add(productPojo);
        productPojo = productService.getByBarcode(productPojo.getBarcode());
        productPojo.setName("name2");
        productPojo.setMrp(123.00);
        productService.update(productPojo, productPojo.getId());
        ProductPojo updatedProductPojo = productService.getByBarcode(productPojo.getBarcode());
        assertEquals(productPojo.getName(),updatedProductPojo.getName());

    }

    @Test
    public void testCheckDuplicates() throws ApiException {
        BrandPojo brandPojo1 = create("new1","cat");
        BrandPojo brandPojo2 = create("new2","cat");
        String error = "";
        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = createProductForm("1q2w3e",brandPojo1.getBrand(),brandPojo1.getCategory(),"name1",100.00);
        productFormList.add(productForm);
        productForm = createProductForm("1q23e",brandPojo1.getBrand(),brandPojo1.getCategory(),"name2",100.00);
        productFormList.add(productForm);
        error = productService.checkDuplicates(productFormList);
        assertEquals(error,"");
    }

    private ProductPojo create(String barcode, Integer brandId, String name, Double mrp){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        productPojo.setName(name);
        productPojo.setBrandId(brandId);
        return productPojo;
    }

    private BrandPojo create(String brand, String category) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        brandService.addBrand(brandPojo);
        return brandService.getBrandByNameAndCategory(brandPojo.getBrand(),brandPojo.getCategory());
    }
    private ProductForm createProductForm(String barcode, String brand,String category, String name, Double mrp ){
        ProductForm productForm = new ProductForm();
        productForm.setName(name);
        productForm.setBarcode(barcode);
        productForm.setBrand(brand);
        productForm.setCategory(category);
        productForm.setMrp(mrp);
        return productForm;
    }

}
