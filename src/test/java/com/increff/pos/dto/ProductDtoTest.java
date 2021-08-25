package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    BrandService brandService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductDto productDto;

    @Test
    public void testAdd() throws ApiException {
        createBrand("new", "cat");
        ProductForm productForm = createForm("1q2w3e", "new", "cat", "product1", 100.00);
        productDto.add(productForm);
        ProductPojo productPojo = productService.getByBarcode(productForm.getBarcode());
        assertNotEquals(null, productPojo);
    }

    @Test
    public void testGetAll() throws ApiException {
        createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        List<ProductData> productDataList = productDto.getAll();
        assertEquals(productDataList.size(), 1);
    }

    @Test
    public void testGetById() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        ProductData productData = productDto.getById(productPojo.getId());
        assertNotEquals(null, productData);
    }

    @Test
    public void testGetByBarcode() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        ProductData productData = productDto.getByBarcode(productPojo.getBarcode());
        assertNotEquals(null, productData);
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        ProductForm productForm = createForm("1q2w3e", "new", "cat", "productNew", 100.00);
        productDto.update(productForm, productPojo.getId());
        productPojo = productService.getById(productPojo.getId());
        assertEquals(productPojo.getName(), productForm.getName());
    }

    @Test
    public void testUpload() throws ApiException {
        createBrand("new", "cat");
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(createForm("1q2w3e", "new", "cat", "productNew", 100.00));
        productFormList.add(createForm("1q2w3r", "new", "cat", "productNew", 100.00));

        productDto.upload(productFormList);
        List<ProductData> productDataList = productDto.getAll();
        assertEquals(productDataList.size(), 2);
    }

    @Test
    public void testCheckDuplicatesRecords() throws ApiException {
        try{

            createBrand("new", "cat");
            List<ProductForm> productFormList = new ArrayList<>();
            productFormList.add(createForm("1q2w3e", "new", "cat", "productNew", 100.00));
            productFormList.add(createForm("1q2w3e", "new", "cat", "productNew", 100.00));

            productDto.upload(productFormList);
            List<ProductData> productDataList = productDto.getAll();
        }
        catch (ApiException apiException){
            assertNotEquals("",apiException.getMessage());
        }
    }

    @Test
    public void testCheckInvalidBrand() throws ApiException {
        try{

            createBrand("new", "cat");
            List<ProductForm> productFormList = new ArrayList<>();
            productFormList.add(createForm("1q2w3e", "new1", "cat", "productNew", 100.00));
            productFormList.add(createForm("1q2w3r", "new", "cat", "productNew", 100.00));

            productDto.upload(productFormList);
            List<ProductData> productDataList = productDto.getAll();
        }
        catch (ApiException apiException){
            assertNotEquals("",apiException.getMessage());
        }
    }

    @Test
    public void testCheckExists() throws ApiException {
        try{
            createBrand("new", "cat");
            ProductForm productForm = createForm("1q2w3e", "new", "cat", "product1", 100.00);
            productDto.add(productForm);
            List<ProductForm> productFormList = new ArrayList<>();
            productFormList.add(createForm("1q2w3e", "new", "cat", "productNew", 100.00));
            productFormList.add(createForm("1q2w3r", "new", "cat", "productNew", 100.00));

            productDto.upload(productFormList);
            List<ProductData> productDataList = productDto.getAll();
        }
        catch (ApiException apiException){
            assertNotEquals("",apiException.getMessage());
        }
    }

    private BrandPojo createBrand(String brand, String category) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandService.addBrand(brandPojo);
    }

    private ProductPojo createProduct(String barcode, String brand, String category, String name, Double mrp) throws ApiException {
        BrandPojo brandPojo = createBrand(brand, category);
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(barcode);
        productPojo.setBrandId(brandPojo.getId());
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        productPojo = productService.add(productPojo);
        return productPojo;
    }

    private ProductForm createForm(String barcode, String brand, String category, String name, Double mrp) {
        ProductForm productForm = new ProductForm();
        productForm.setCategory(category);
        productForm.setBarcode(barcode);
        productForm.setBrand(brand);
        productForm.setName(name);
        productForm.setMrp(mrp);
        return productForm;
    }

}
