package com.increff.pos.service;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.pojo.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderServiceTest extends AbstractUnitTest {
    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    OrderService orderService;

    @Test
    public void testCreate() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),100,100.00));
        orderService.create(orderItemPojoList);
    }

    @Test
    public void testGetById() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),100,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        List<OrderItemPojo> getOrderItemPojoList = orderService.getById(orderPojo.getId());
        assertEquals(orderItemPojoList,getOrderItemPojoList);
    }

    @Test
    public void testGetAll() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),100,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        List<OrderPojo> orderPojoList = orderService.getAll();
        assertEquals(orderPojoList.get(0),orderPojo);
    }

    @Test
    public void testIsInvoiceGenerated() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),100,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        List<OrderPojo> orderPojoList = orderService.getAll();
        assertEquals(orderPojoList.get(0).getInvoiceGenerated(),orderService.isInvoiceGenerated(orderPojo.getId()));
    }

    @Test
    public void testGetOnlyOrderById() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),100,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        OrderPojo orderPojo1 = orderService.getOnlyOrderById(orderPojo.getId());
        assertEquals(orderPojo1,orderPojo);
    }

    @Test
    public void testGenerateInvoice() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),100,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        orderService.generateInvoice(orderPojo.getId());
        OrderPojo orderPojo1 = orderService.getOnlyOrderById(orderPojo.getId());
        assertTrue(orderPojo1.getInvoiceGenerated());
    }

    @Test
    public void testGetAllInvoiceGeneratedOrder() throws ApiException {
        BrandPojo brandPojo = create("new","cat");
        ProductPojo productPojo = create("1e3r4",brandPojo.getId(),"name",123.03);
        InventoryPojo inventoryPojo = create(productPojo.getId(),100);
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(create(productPojo.getId(),100,100.00));
        OrderPojo orderPojo = orderService.create(orderItemPojoList);
        orderService.generateInvoice(orderPojo.getId());
        orderPojo = orderService.getOnlyOrderById(orderPojo.getId());
        List<OrderPojo> orderPojoList = orderService.getAllInvoiceGeneratedOrder(true);
        assertEquals(orderPojoList.get(0),orderPojo);
    }


    private ProductPojo create(String barcode, int brandId, String name, Double mrp) throws ApiException {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        productPojo.setName(name);
        productPojo.setBrandId(brandId);
        return productService.add(productPojo);
    }

    private BrandPojo create(String brand, String category) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        brandService.addBrand(brandPojo);
        return brandService.getBrandByNameAndCategory(brandPojo.getBrand(),brandPojo.getCategory());
    }

    private InventoryPojo create(int id, int quantity) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(quantity);
        inventoryPojo.setProductId(id);
        return inventoryService.add(inventoryPojo);
    }

    private OrderItemPojo create(int productId,int quantity,double sellingPrice){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setSellingPrice(sellingPrice);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setProductId(productId);
        return orderItemPojo;
    }
}
