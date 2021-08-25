package com.increff.pos.utils;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.data.*;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static org.junit.Assert.*;


public class ConvertUtilTest extends AbstractUnitTest {

    private static final double DELTA = 1e-15;

    @Autowired
    BrandService brandService;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    OrderService orderService;

    @Test
    public void testBrandPojoToBrandData() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("new","cat");
        BrandData brandData = ConvertUtil.convert(brandPojo);
        assertEquals(brandData.getBrand(),brandPojo.getBrand());
        assertEquals(brandData.getCategory(),brandPojo.getCategory());
    }

    @Test
    public void testBrandFormToBrandPojo() throws ApiException {
        BrandForm brandForm = createBrandForm("new","cat");
        BrandPojo brandPojo = ConvertUtil.convert(brandForm);
        assertEquals(brandPojo.getBrand(),brandPojo.getBrand());
        assertEquals(brandPojo.getCategory(),brandPojo.getCategory());
    }

    @Test
    public  void testProductPojoToProductFrom() throws ApiException{
        BrandPojo brandPojo = createBrandPojo("new","cat");
        brandPojo = brandService.addBrand(brandPojo);
        ProductPojo productPojo = createProductPojo("1q2w3e",brandPojo.getId(),"name",100.00);
        ProductData productData = ConvertUtil.convert(productPojo,"new","cat");
        assertEquals(productData.getBarcode(),productPojo.getBarcode());
        assertEquals(productData.getName(),productPojo.getName());
        assertEquals(productData.getMrp(),productPojo.getMrp(),DELTA);
    }

    @Test
    public void testProductFormToProductPojo() throws ApiException{
        BrandPojo brandPojo = createBrandPojo("new","cat");
        brandPojo = brandService.addBrand(brandPojo);
        ProductForm productForm = createProductForm("1q2w3e",brandPojo.getBrand(),
                brandPojo.getCategory(),"name",100.00);
        ProductPojo productPojo = ConvertUtil.convert(productForm, brandPojo.getId());
        assertEquals(productForm.getBarcode(),productPojo.getBarcode());
        assertEquals(productForm.getName(),productPojo.getName());
        assertEquals(productForm.getMrp(),productPojo.getMrp(),DELTA);
    }

    @Test
    public void testInventoryPojoToInventoryForm() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("new","cat");
        brandPojo = brandService.addBrand(brandPojo);
        ProductPojo productPojo = createProductPojo("1q2w3e",brandPojo.getId(),"name",100.00);
        productPojo = productService.add(productPojo);
        InventoryPojo inventoryPojo = createInventoryPojo(productPojo.getId(),100);
        productPojo = productService.getById(inventoryPojo.getProductId());
        InventoryData inventoryData = ConvertUtil.convert(inventoryPojo,productPojo.getBarcode(),productPojo.getName());
        assertEquals(inventoryData.getQuantity().intValue(),inventoryPojo.getQuantity().intValue());
    }

    @Test
    public void testInventoryFormToInventoryPojo() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("new","cat");
        brandPojo = brandService.addBrand(brandPojo);
        ProductPojo productPojo = createProductPojo("1q2w3e",brandPojo.getId(),"name",100.00);
        productPojo = productService.add(productPojo);
        InventoryForm inventoryForm = createInventoryForm(productPojo.getBarcode(),100);
        productPojo = productService.getByBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = ConvertUtil.convert(inventoryForm,productPojo.getId());
        assertEquals(inventoryPojo.getQuantity().intValue(),inventoryForm.getQuantity().intValue());
    }

    @Test
    public void testOrderPojoToOrderForm(){
        OrderPojo orderPojo = createOrderPojo();
        OrderData orderData = ConvertUtil.convert(orderPojo);
        assertEquals((orderData.getOrderId()),orderPojo.getId());
        assertEquals(orderData.getOrderTime(),orderPojo.getOrderTime());
        assertEquals(orderData.getInvoiceGenerated(),orderPojo.getInvoiceGenerated());
    }

    @Test
    public void testOrderItemPojoToOrderItemData() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("new","cat");
        brandPojo = brandService.addBrand(brandPojo);
        ProductPojo productPojo = createProductPojo("1q2w3e",brandPojo.getId(),"name",100.00);
        productPojo = productService.add(productPojo);
        InventoryPojo inventoryPojo = createInventoryPojo(productPojo.getId(),100);
        inventoryPojo = inventoryService.add(inventoryPojo);
        OrderPojo orderPojo = createOrderPojo();
        OrderItemPojo orderItemPojo = createOrderItemPojo(0,100.00,10, productPojo.getId());
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(orderItemPojo);
        orderPojo = orderService.create(orderItemPojoList);
        OrderItemData orderItemData = ConvertUtil.convert(orderItemPojo);
        assertEquals(orderItemData.getOrderId().intValue(),orderItemPojo.getOrderId().intValue());
        assertEquals(orderItemData.getQuantity().intValue(),orderItemPojo.getQuantity().intValue());
        assertEquals(orderItemData.getSellingPrice(),orderItemPojo.getSellingPrice(),DELTA);
    }

    @Test
    public void testOrderItemFormToOrderItemPojo() throws ApiException {
        BrandPojo brandPojo = createBrandPojo("new","cat");
        brandPojo = brandService.addBrand(brandPojo);
        ProductPojo productPojo = createProductPojo("1q2w3e",brandPojo.getId(),"name",100.00);
        productPojo = productService.add(productPojo);
        InventoryPojo inventoryPojo = createInventoryPojo(productPojo.getId(),100);
        inventoryPojo = inventoryService.add(inventoryPojo);
        OrderPojo orderPojo = createOrderPojo();
        OrderItemPojo orderItemPojo = createOrderItemPojo(0,100.00,10, productPojo.getId());
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        orderItemPojoList.add(orderItemPojo);
        orderPojo = orderService.create(orderItemPojoList);
        OrderItemForm orderItemForm = createOrderItemForm(orderPojo.getId(),100.00,10,productPojo.getBarcode());
        productPojo = productService.getByBarcode(orderItemForm.getBarcode());
        orderItemPojo = ConvertUtil.convert(orderItemForm,productPojo.getId());
        assertEquals((orderItemForm.getOrderId().intValue()),orderItemPojo.getOrderId().intValue());
        assertEquals((orderItemForm.getQuantity().intValue()),orderItemPojo.getQuantity().intValue());
        assertEquals(orderItemForm.getBarcode(),orderItemForm.getBarcode());
    }

    @Test
    public void testStringToZoneDateTime(){
        String date = "11/11/2020";
        String[] dateList = date.split("/");
        ZonedDateTime zonedDateTime = ConvertUtil.convert(dateList,"start");
        LocalDate localDate = LocalDate.of(parseInt(dateList[2]), parseInt(dateList[1]), parseInt(dateList[0]));
        LocalTime localTime = LocalTime.of(0, 0);
        ZoneId zoneId = ZoneId.of("GMT+05:30");
        assertEquals(zonedDateTime,ZonedDateTime.of(localDate, localTime, zoneId));
    }


    private BrandPojo createBrandPojo(String brand, String category) throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    private BrandForm createBrandForm(String brand, String category) throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    private ProductPojo createProductPojo(String barcode, Integer brandId, String name, Double mrp){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        productPojo.setName(name);
        productPojo.setBrandId(brandId);
        return productPojo;
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

    private InventoryPojo createInventoryPojo(int productId,int quantity){
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

    private InventoryForm createInventoryForm(String barcode,int quantity){
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }

    private OrderPojo createOrderPojo(){
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setOrderTime(ZonedDateTime.now());
        orderPojo.setInvoiceGenerated(false);
        return orderPojo;
    }

    private OrderItemPojo createOrderItemPojo(int orderId,Double sellingPrice,int quantity,int productId){
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setSellingPrice(sellingPrice);
        orderItemPojo.setQuantity(quantity);
        orderItemPojo.setProductId(productId);
        return orderItemPojo;
    }
    private OrderItemForm createOrderItemForm(int orderId,Double sellingPrice,int quantity,String barcode ){
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setOrderId(orderId);
        orderItemForm.setSellingPrice(sellingPrice);
        orderItemForm.setQuantity(quantity);
        orderItemForm.setBarcode(barcode);
        return orderItemForm;
    }
}
