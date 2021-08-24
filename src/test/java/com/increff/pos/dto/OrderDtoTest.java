package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.*;
import com.increff.pos.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDtoTest extends AbstractUnitTest {

    private static final double DELTA = 1e-15;

    @Autowired
    BrandService brandService;

    @Autowired
    ProductService productService;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDto orderDto;

    @Test
    public void testAdd() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        InventoryPojo inventoryPojo = createInventory(productPojo.getId(), 100);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(createOrderItemForm(null, null, productPojo.getBarcode(),
                10, 10.00));
        OrderForm orderForm = createOrderForm(null, orderItemFormList);
        OrderData orderData = orderDto.add(orderForm);

        List<OrderItemPojo> orderItemPojoList = orderService.getById(orderData.getOrderId());
        assertEquals(orderItemPojoList.size(), 1);
    }

    @Test
    public void testGetAll() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        InventoryPojo inventoryPojo = createInventory(productPojo.getId(), 100);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(createOrderItemForm(null, null, productPojo.getBarcode(),
                10, 10.00));
        OrderForm orderForm = createOrderForm(null, orderItemFormList);
        orderDto.add(orderForm);
        List<OrderData> orderDataList = orderDto.getAll();
        assertEquals(orderDataList.size(), 1);
    }

    @Test
    public void testGetById() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        InventoryPojo inventoryPojo = createInventory(productPojo.getId(), 100);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(createOrderItemForm(null, null, productPojo.getBarcode(),
                10, 10.00));
        OrderForm orderForm = createOrderForm(null, orderItemFormList);
        OrderData orderData = orderDto.add(orderForm);
        List<OrderItemData> orderItemDataList = orderDto.getById(orderData.getOrderId());

        assertEquals(orderItemDataList.size(), 1);
    }

    @Test
    public void testGenerate() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        InventoryPojo inventoryPojo = createInventory(productPojo.getId(), 100);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(createOrderItemForm(null, null, productPojo.getBarcode(),
                10, 10.00));
        OrderForm orderForm = createOrderForm(null, orderItemFormList);
        OrderData orderData = orderDto.add(orderForm);
        orderDto.generateInvoice(orderData.getOrderId());
        OrderPojo orderPojo = orderService.getOnlyOrderById(orderData.getOrderId());

        assertTrue(orderPojo.getInvoiceGenerated());

    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo productPojo = createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        InventoryPojo inventoryPojo = createInventory(productPojo.getId(), 100);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(createOrderItemForm(null, null, productPojo.getBarcode(),
                10, 10.00));
        OrderForm orderForm = createOrderForm(null, orderItemFormList);
        OrderData orderData = orderDto.add(orderForm);
        List<OrderItemData> orderItemDataList = orderDto.getById(orderData.getOrderId());
        orderItemFormList = new ArrayList<>();
        orderItemFormList.add(createOrderItemForm(orderItemDataList.get(0).getOrderItemId(), orderItemDataList.get(0).getOrderId(),
                productPojo.getBarcode(), 10, 99.00));
        orderForm.setId(orderData.getOrderId());
        orderForm.setOrderItemList(orderItemFormList);
        orderDto.update(orderForm);
        orderItemDataList = orderDto.getById(orderData.getOrderId());

        assertEquals(orderItemDataList.get(0).getSellingPrice(), 99.00,DELTA);

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
        return productService.add(productPojo);
    }

    private InventoryPojo createInventory(Integer id, Integer quantity) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(id);
        inventoryPojo.setQuantity(quantity);
        return inventoryService.add(inventoryPojo);
    }

    private OrderItemForm createOrderItemForm(Integer orderItemId, Integer orderId, String barcode, Integer quantity,
                                              Double sellingPrice) {

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setOrderItemId(orderItemId);
        orderItemForm.setOrderId(orderId);
        orderItemForm.setBarcode(barcode);
        orderItemForm.setQuantity(quantity);
        orderItemForm.setSellingPrice(sellingPrice);

        return orderItemForm;
    }

    private OrderForm createOrderForm(Integer id, List<OrderItemForm> orderItemFormList) {
        OrderForm orderForm = new OrderForm();
        orderForm.setOrderItemList(orderItemFormList);
        orderForm.setId(id);
        return orderForm;
    }


}
