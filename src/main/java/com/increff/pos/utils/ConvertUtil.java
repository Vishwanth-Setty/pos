package com.increff.pos.utils;

import com.increff.pos.model.data.*;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.ApiException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.lang.Integer.parseInt;

public class ConvertUtil {


    public static BrandData convert(BrandPojo p) {
        BrandData d = new BrandData();
        d.setId(p.getId());
        d.setBrand(p.getBrand());
        d.setCategory(p.getCategory());
        return d;
    }

    public static BrandPojo convert(BrandForm form) {
        BrandPojo p = new BrandPojo();
        p.setBrand(form.getBrand());
        p.setCategory(form.getCategory());
        return p;
    }

    public static ProductPojo convert(ProductForm productForm, int brandId) throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(productForm.getBarcode());
        productPojo.setName(productForm.getName());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setBrandId(brandId);
        return productPojo;
    }

    public static ProductData convert(ProductPojo productPojo, String brand, String category) throws ApiException {
        ProductData productData = new ProductData();
        productData.setId(productPojo.getId());
        productData.setBarcode(productPojo.getBarcode());
        productData.setName(productPojo.getName());
        productData.setMrp(productPojo.getMrp());
        productData.setBrand(brand);
        productData.setCategory(category);
        return productData;
    }

    public static InventoryData convert(InventoryPojo inventoryPojo, String barcode, String name) {
        InventoryData inventoryData = new InventoryData();
        inventoryData.setBarcode(barcode);
        inventoryData.setProductName(name);
        inventoryData.setQuantity(inventoryPojo.getQuantity());
        return inventoryData;
    }

    public static InventoryPojo convert(InventoryForm inventoryForm, int id) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(id);
        inventoryPojo.setQuantity(inventoryForm.getQuantity());
        return inventoryPojo;
    }

    public static OrderData convert(OrderPojo orderPojo) {
        OrderData orderData = new OrderData();
        orderData.setOrderId(orderPojo.getId());
        orderData.setOrderTime(orderPojo.getOrderTime());
        orderData.setInvoiceGenerated(orderPojo.getInvoiceGenerated());
        return orderData;
    }

    public static OrderItemData convert(OrderItemPojo orderItemPojo) {
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setOrderItemId(orderItemPojo.getId());
        orderItemData.setQuantity(orderItemPojo.getQuantity());
        orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        return orderItemData;
    }

    public static OrderItemPojo convert(OrderItemForm orderItemForm, int productId) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setQuantity(orderItemForm.getQuantity());
        orderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
        orderItemPojo.setProductId(productId);
        if(orderItemForm.getOrderItemId()==null || orderItemForm.getOrderItemId()==0){
            orderItemPojo.setId(null);
        }
        else{
            orderItemPojo.setId(orderItemForm.getOrderItemId());
        }
        orderItemPojo.setOrderId(orderItemForm.getOrderId());
        return orderItemPojo;
    }

    public static OrderItemXml covert(OrderItemData orderItemData, Double mrp) {
        OrderItemXml orderItemXml = new OrderItemXml();
        orderItemXml.setBarcode(orderItemData.getBarcode());
        orderItemXml.setMrp(mrp);
        orderItemXml.setQuantity(orderItemData.getQuantity());
        orderItemXml.setPerUnitSellingPrice(orderItemData.getSellingPrice());
        orderItemXml.setTotalSellingPrice(orderItemData.getQuantity()*orderItemData.getSellingPrice());
        return orderItemXml;
    }

    public static ZonedDateTime convert(String[] date, String type) {
        LocalDate localDate = LocalDate.of(parseInt(date[2]), parseInt(date[1]), parseInt(date[0]));
        LocalTime localTime = LocalTime.of(0, 0);
        if (type.equals("start"))
            localTime = LocalTime.of(0, 0);
        if (type.equals("end"))
            localTime = LocalTime.of(23, 59);
        ZoneId zoneId = ZoneId.of("GMT+05:30");
        return ZonedDateTime.of(localDate, localTime, zoneId);
    }

}
