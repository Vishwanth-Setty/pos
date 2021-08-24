package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    BrandService brandService;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    InventoryDto inventoryDto;

    @Test
    public void testAdd() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e","new","cat","product1",100.00);
        InventoryForm inventoryForm = createForm("1q2w3e",100);
        inventoryDto.add(inventoryForm);
        InventoryPojo inventoryPojo = inventoryService.getById(productPojo.getId());
        assertNotEquals(null,inventoryPojo);
    }

    @Test
    public void testGetAll() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e","new","cat","product1",100.00);
        InventoryPojo inventoryPojo = createInventory(productPojo.getId(),100);
        List<InventoryData> inventoryDataList = inventoryDto.getAll();
        assertEquals(inventoryDataList.size(),1);
    }

    @Test
    public void testGet() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e","new","cat","product1",100.00);
        InventoryPojo inventoryPojo = createInventory(productPojo.getId(),100);
        InventoryData inventoryData = inventoryDto.get(productPojo.getBarcode());
        assertNotEquals(null,inventoryData);
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e","new","cat","product1",100.00);
        InventoryPojo inventoryPojo = createInventory(productPojo.getId(),100);
        InventoryForm inventoryForm = createForm(productPojo.getBarcode(),110);
        inventoryDto.update(inventoryForm);
        inventoryPojo = inventoryService.getById(productPojo.getId());
        assertEquals(inventoryPojo.getQuantity().intValue(),110);
    }
    public void testUpload() throws ApiException {
        createProduct("1q2w3e","new","cat","product1",100.00);
        createProduct("1q2w3r","new","cat","product1",100.00);

        List<InventoryForm> inventoryFormList = new ArrayList<>();
        inventoryFormList.add(createForm("1q2w3e",110));
        inventoryFormList.add(createForm("1q2w3r",110));
        inventoryDto.upload(inventoryFormList);
        List<InventoryData> inventoryDataList = inventoryDto.getAll();

        assertEquals(inventoryDataList.size(),2);
    }


    private BrandPojo createBrand(String brand, String category) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandService.addBrand(brandPojo);
    }

    private ProductPojo createProduct(String barcode, String brand, String category, String name, Double mrp) throws ApiException {
        BrandPojo brandPojo = createBrand(brand,category);
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(barcode);
        productPojo.setBrandId(brandPojo.getId());
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        productPojo = productService.add(productPojo);
        return productPojo;
    }

    private InventoryPojo createInventory(Integer id,Integer quantity) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(id);
        inventoryPojo.setQuantity(quantity);
        return inventoryService.add(inventoryPojo);
    }

    private InventoryForm createForm(String barcode, Integer quantity){
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }

}
