package com.increff.pos.service;


import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    ProductService productService;

    public void testAdd(){
//        pro
//        ProductPojo productPojo = create("1q2w3e",);
    }

    private ProductPojo createProduct(String barcode, Integer brandId, String name, Double mrp){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        productPojo.setName(name);
        productPojo.setBrandId(brandId);
        return productPojo;
    }

}
