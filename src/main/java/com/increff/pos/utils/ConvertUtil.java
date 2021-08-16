package com.increff.pos.utils;

import com.increff.pos.model.data.*;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

public class ConvertUtil {

    public static BrandData convert(BrandPojo p){
        BrandData d = new BrandData();
        d.setId(p.getId());
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        return d;
    }

    public static BrandPojo convert(BrandForm form){
        BrandPojo p = new BrandPojo();
        p.setBrand(form.getBrand());
        p.setCategory(form.getCategory());
        return p;
    }
    public static ProductPojo convert(ProductForm productForm) throws ApiException {
        BrandPojo brandPojo = brandService.getBrandByNameAndCategory(productForm.getBrand(),productForm.getCategory());
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBrandId(brandPojo.getId());
        return productPojo;
    }

    public static ProductData convert(ProductPojo productPojo) throws ApiException{
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setBarcode(productPojo.getBarcode());
        productData.setName(productPojo.getName());
        productData.setMrp(productPojo.getMrp());
        productData.setBrandId(productPojo.getBrandId());
        return productData;
    }

    public static InventoryData convert(InventoryPojo inventoryPojo){
        ProductPojo productPojo = productService.getProduct(inventoryPojo.getProductId());
        InventoryData inventoryData = new InventoryData();
        inventoryData.setBarcode(productPojo.getBarcode());
        inventoryData.setProductName(productPojo.getName());
        inventoryData.setProductId(productPojo.getId());
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }

    public static InventoryPojo convert(InventoryForm inventoryForm){
        ProductPojo productPojo = productService.getProductByBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productPojo.getId());
        inventoryPojo.setQuantity(inventoryPojo.getQuantity());
        return inventoryPojo;
    }

    public static OrderData convert(OrderPojo orderPojo){
        OrderData orderData = new OrderData();
        orderData.setOrderId(orderPojo.getId());
        orderData.setOrderTime(orderPojo.getOrderTime());
        orderData.setInvoiceGenerated(false);
        return orderData;
    }

    public static OrderItemData convert(OrderItemPojo orderItemPojo){
        OrderItemData orderItemData = new OrderItemData();
//        String barcode = productService.getProduct(orderItemPojo.getProductId()).getBarcode();
        orderItemData.setOrderItemId(orderItemPojo.getId());
//        orderItemData.setBarcode(barcode);
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        return orderItemData;
    }

    public static OrderItemPojo convert(OrderItemForm orderItemForm){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        ProductPojo productPojo = productService.getProductByBarcode(orderItemForm.getBarcode());
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        orderItemPojo.setProductId(productPojo.getId());
        orderItemPojo.setId(orderItemForm.getId());
        orderItemPojo.setOrderId(orderItemForm.getOrderId());
        return orderItemPojo;
    }
}
