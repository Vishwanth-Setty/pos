package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class inventoryServiceTest extends AbstractUnitTest {
    @Autowired
    ProductService productService;
    @Autowired
    BrandService brandService;
    @Autowired
    InventoryService inventoryService;

    @Test
    public void testAdd() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        productPojo = productService.add(productPojo);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        inventoryService.add(inventoryPojo);
    }

    @Test
    public void testGetById() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        productPojo = productService.add(productPojo);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        inventoryService.add(inventoryPojo);
        InventoryPojo inventoryPojo1 = inventoryService.getById(productPojo.getId());
        assertEquals(inventoryPojo1,inventoryPojo);
    }

    @Test
    public void testGetAll() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        productPojo = productService.add(productPojo);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        inventoryService.add(inventoryPojo);
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        assertEquals(inventoryPojoList.get(0),inventoryPojo);
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        productPojo = productService.add(productPojo);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        inventoryPojo = inventoryService.add(inventoryPojo);
        inventoryPojo.setQuantity(110);
        inventoryService.update(inventoryPojo);
        InventoryPojo inventoryPojo1 = inventoryService.getById(inventoryPojo.getProductId());
        assertEquals(inventoryPojo1.getQuantity(),inventoryPojo.getQuantity());
    }

    private ProductPojo create(String barcode, int brandId, String name, Double mrp) throws ApiException {
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

    private InventoryPojo create(int id,int quantity){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(quantity);
        inventoryPojo.setProductId(id);
        return inventoryPojo;
    }

}
