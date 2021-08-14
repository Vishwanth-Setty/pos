package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ProductService productService;
    @Autowired
    InventoryService inventoryService;

    CopyUtil copyUtil;

    @Transactional
    public List<OrderPojo> getAll() {
        return orderDao.selectAll();
    }

    @Transactional
    public List<OrderItemData> getById(int id) {
        List<OrderItemPojo> orderItemPojoListList = orderDao.select(id);
        List<OrderItemData> orderItemDataList = convert(orderItemPojoListList);
        return orderItemDataList;
    }

    @Transactional
    public void create(OrderForm orderForm) throws ApiException {
        boolean check = checkInventory(orderForm);
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setOrderTime(ZonedDateTime.now());
        int orderId = orderDao.insert(orderPojo);
        orderForm.setId(orderId);
        List<OrderItemPojo> orderItemPojoList = convert(orderForm);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemService.create(orderItemPojo);
            decreaseInventory(orderItemPojo);
        }
    }

    @Transactional
    public void update(OrderForm orderForm) throws ApiException {
        boolean check = checkInventory(orderForm);
        List<OrderItemPojo> orderItemPojoList = convert(orderForm);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            if (orderItemPojo.getOrderId() == 0) {
                orderItemService.create(orderItemPojo);
                decreaseInventory(orderItemPojo);
            } else {
                OrderItemPojo orderItemPojoOld = orderItemService.selectById(orderItemPojo.getId());
                copyUtil.copy(orderItemPojo, orderItemPojoOld);
                decreaseInventory(orderItemPojoOld, orderItemPojo);
                if (orderItemPojo.getQuantity() == 0) {
                    orderItemPojoOld.setOrderId(0);
                }
            }
        }
    }

    @Transactional
    private boolean checkInventory(OrderForm orderForm) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = convert(orderForm);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            InventoryData inventoryData = inventoryService.getInventory(orderItemPojo.getProductId());
            OrderItemPojo orderItemPojoOld;
            int reqQuantity = orderItemPojo.getQuantity();
            if (orderItemPojo.getOrderId() != 0) {
                orderItemPojoOld = orderItemService.selectById(orderItemPojo.getId());
                reqQuantity = reqQuantity - orderItemPojoOld.getQuantity();
            }
            if (inventoryData.getQuantity() < reqQuantity) {
                throw new ApiException("Not enough Quantity is available in inventory");
            }
        }
        return false;
    }

    private List<OrderItemPojo> convert(OrderForm orderForm) {
        List<OrderItemPojo> orderItemFormList = new ArrayList<OrderItemPojo>();
        for (OrderItemForm orderItemForm : orderForm.getOrderItemList()) {
            OrderItemPojo tempOrderItemPojo = new OrderItemPojo();
            int productId = productService.getProductByBarcode(orderItemForm.getBarcode()).getId();
            tempOrderItemPojo.setProductId(productId);
            tempOrderItemPojo.setOrderId(orderForm.getId());
            tempOrderItemPojo.setQuantity(orderItemForm.getQuantity());
            tempOrderItemPojo.setSellingPrice(orderItemForm.getSellingPrice());
            orderItemFormList.add(tempOrderItemPojo);
        }
        return orderItemFormList;
    }

    private List<OrderItemData> convert(List<OrderItemPojo> orderItemPojoList) {
        List<OrderItemData> orderItemDataList = new ArrayList<OrderItemData>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            OrderItemData tempOrderItemData = new OrderItemData();
            String barcode = productService.getProduct(orderItemPojo.getProductId()).getBarcode();
            tempOrderItemData.setOrderItemId(orderItemPojo.getId());
            tempOrderItemData.setBarcode(barcode);
            tempOrderItemData.setQuantity(orderItemPojo.getQuantity());
            tempOrderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
            tempOrderItemData.setOrderId(orderItemPojo.getOrderId());
            orderItemDataList.add(tempOrderItemData);

        }
        return orderItemDataList;
    }


    private void decreaseInventory(OrderItemPojo orderItemPojo) {
        InventoryData inventoryData = inventoryService.getInventory(orderItemPojo.getProductId());
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(inventoryData.getQuantity() - orderItemPojo.getQuantity());
        inventoryPojo.setProductId(inventoryData.getProductId());
        inventoryService.updateInventory(inventoryPojo);
    }

    private void decreaseInventory(OrderItemPojo orderItemPojo, OrderItemPojo newOrderItemPojo) {
        InventoryData inventoryData = inventoryService.getInventory(orderItemPojo.getProductId());
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(inventoryData.getQuantity() - orderItemPojo.getQuantity() + newOrderItemPojo.getQuantity());
        inventoryPojo.setProductId(inventoryData.getProductId());
        inventoryService.updateInventory(inventoryPojo);
    }
}
