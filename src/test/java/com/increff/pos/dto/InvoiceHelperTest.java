package com.increff.pos.dto;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class InvoiceHelperTest extends AbstractUnitTest {

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


    @Autowired
    InvoiceHelper invoiceHelper;

    @Test
    public void testGenerate() throws Exception {
        ProductPojo productPojo = createProduct("1q2w3e", "new", "cat", "product1", 100.00);
        InventoryPojo inventoryPojo = createInventory(productPojo.getId(), 100);
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(createOrderItemForm(null, null, productPojo.getBarcode(),
                10, 10.00));
        OrderForm orderForm = createOrderForm(null, orderItemFormList);
        OrderData orderData = orderDto.add(orderForm);
        orderDto.generateInvoice(orderData.getOrderId());
        invoiceHelper.downloadInvoice(orderData.getOrderId());
        OrderPojo orderPojo = orderService.getOnlyOrderById(orderData.getOrderId());
        assertTrue(orderPojo.getInvoiceGenerated());

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
