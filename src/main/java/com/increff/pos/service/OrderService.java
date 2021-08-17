package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    InventoryService inventoryService;

    CopyUtil copyUtil;

    @Transactional
    public List<OrderPojo> getAll() {
        return orderDao.selectAll();
    }

    @Transactional
    public List<OrderItemPojo> getById(int id) {
        return orderDao.select(id);
    }

    @Transactional
    public void create(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        checkInventory(orderItemPojoList);
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setOrderTime(ZonedDateTime.now());
        int orderId = orderDao.insert(orderPojo);
        for(OrderItemPojo orderItemPojo : orderItemPojoList){
            orderItemPojo.setOrderId(orderId);
            orderItemService.create(orderItemPojo);
            updateInventory(orderItemPojo);
        }
    }

    @Transactional
    public void update(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        checkInventory(orderItemPojoList);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            if (orderItemPojo.getOrderId() == 0) {
                orderItemService.create(orderItemPojo);
                updateInventory(orderItemPojo);
            } else {
                OrderItemPojo orderItemPojoOld = orderItemService.selectById(orderItemPojo.getId());
                copyUtil.copy(orderItemPojo, orderItemPojoOld);
                updateInventory(orderItemPojoOld, orderItemPojo);
                if (orderItemPojo.getQuantity() == 0) {
                    orderItemPojoOld.setOrderId(0);
                }
            }
        }
    }

    @Transactional
    private void checkInventory(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            InventoryPojo inventoryPojo = inventoryService.getById(orderItemPojo.getProductId());
            int reqQuantity = orderItemPojo.getQuantity();
            if (inventoryPojo.getQuantity() < reqQuantity) {
                throw new ApiException("Not enough Quantity is available in inventory");
            }
        }
    }

    private void updateInventory(OrderItemPojo orderItemPojo) {
        InventoryPojo inventoryPojo = inventoryService.getById(orderItemPojo.getProductId());
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemPojo.getQuantity());
        inventoryService.update(inventoryPojo);
    }

    private void updateInventory(OrderItemPojo orderItemPojo, OrderItemPojo newOrderItemPojo) {
        InventoryPojo inventoryPojo = inventoryService.getById(orderItemPojo.getProductId());
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemPojo.getQuantity() + newOrderItemPojo.getQuantity());
        inventoryService.update(inventoryPojo);
    }
}
