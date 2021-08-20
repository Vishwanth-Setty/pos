package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService extends ValidateUtils {

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

//        checkInvoice(orderItemPojoList);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            if (orderItemPojo.getId() == 0) {
                orderItemService.create(orderItemPojo);
                updateInventory(orderItemPojo);
            } else {
                OrderItemPojo orderItemPojoOld = orderItemService.selectById(orderItemPojo.getId());
                updateInventory(orderItemPojoOld, orderItemPojo);
                orderItemPojoOld.setQuantity(orderItemPojo.getQuantity());
                orderItemPojoOld.setSellingPrice(orderItemPojo.getSellingPrice());
                if (orderItemPojo.getQuantity() == 0) {
                    orderItemPojoOld.setOrderId(0);
                }
            }
        }
    }

    @Transactional
    public boolean isInvoiceGenerated(int orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.selectById(orderId);
        checkNotNull(orderPojo,"Invalid Order Id");
        return orderPojo.isInvoiceGenerated();
    }

    @Transactional
    public OrderPojo getOnlyOrderById(int orderId) {
        return orderDao.selectById(orderId);
    }

    @Transactional
    public void generateInvoice(int orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.selectById(orderId);
        checkNotNull(orderPojo,"Invalid Order Id");
        orderPojo.setInvoiceGenerated(true);
    }

    @Transactional
    public List<OrderPojo> getAllInvoiceGeneratedOrder(boolean invoiceGenerated){
        return orderDao.selectAllWithInvoiceGenerated(invoiceGenerated);
    }

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
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() + orderItemPojo.getQuantity() - newOrderItemPojo.getQuantity());
        inventoryService.update(inventoryPojo);
    }

}
