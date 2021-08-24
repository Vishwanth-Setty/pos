package com.increff.pos.service;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.utils.AbstractApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)

public class OrderService extends AbstractApi {

    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderItemDao orderItemDao;
    @Autowired
    InventoryService inventoryService;

    @Autowired
    ProductService productService;

    public List<OrderPojo> getAll() {
        return orderDao.selectAll();
    }

    public List<OrderItemPojo> getById(int id) {
        return orderDao.selectOrderDetails(id);
    }

    public OrderPojo create(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        checkInventory(orderItemPojoList);
        checkSellingPrice(orderItemPojoList);

        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setOrderTime(ZonedDateTime.now());
        orderPojo.setInvoiceGenerated(false);
        orderPojo = orderDao.persist(orderPojo);

        for(OrderItemPojo orderItemPojo : orderItemPojoList){
            orderItemPojo.setOrderId(orderPojo.getId());
            orderItemDao.persist(orderItemPojo);
            updateInventory(orderItemPojo);
        }
        return orderPojo;
    }

    public void update(List<OrderItemPojo> orderItemPojoList) throws ApiException {

        if(checkInvoice(orderItemPojoList.get(0).getOrderId())){
            throw new ApiException("Invoice is Generated");
        }
        checkSellingPrice(orderItemPojoList);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            if (orderItemPojo.getId() == 0) {
                orderItemDao.persist(orderItemPojo);
                updateInventory(orderItemPojo);

            } else {
                OrderItemPojo orderItemPojoOld = orderItemDao.select(orderItemPojo.getId());
                updateInventory(orderItemPojoOld, orderItemPojo);
                orderItemPojoOld.setQuantity(orderItemPojo.getQuantity());
                orderItemPojoOld.setSellingPrice(orderItemPojo.getSellingPrice());
            }
        }
    }

    public Boolean isInvoiceGenerated(int orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.select(orderId);
        checkNotNull(orderPojo,"Invalid Order Id");
        return orderPojo.getInvoiceGenerated();
    }

    public OrderPojo getOnlyOrderById(int orderId) {
        return orderDao.select(orderId);
    }

    public void generateInvoice(int orderId) throws ApiException {
        OrderPojo orderPojo = orderDao.select(orderId);
        checkNotNull(orderPojo,"Invalid Order Id");
        orderPojo.setInvoiceGenerated(true);
    }

    public List<OrderPojo> getAllInvoiceGeneratedOrder(boolean invoiceGenerated){
        return orderDao.selectByMethod("invoiceGenerated",invoiceGenerated);
    }

    private void checkInventory(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            InventoryPojo inventoryPojo = inventoryService.getById(orderItemPojo.getProductId());
            int reqQuantity = orderItemPojo.getQuantity();
            if (inventoryPojo.getQuantity() < reqQuantity) {
                ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                throw new ApiException("Insufficient inventory for barcode "+productPojo.getBarcode());
            }
        }
    }

    private void updateInventory(OrderItemPojo orderItemPojo) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.getById(orderItemPojo.getProductId());
        int quantity = inventoryPojo.getQuantity() - orderItemPojo.getQuantity();

        if(quantity<0){
            ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
            throw new ApiException("Insufficient inventory for barcode "+productPojo.getBarcode());
        }
        inventoryPojo.setQuantity(quantity);
        inventoryService.update(inventoryPojo);
    }

    private void updateInventory(OrderItemPojo orderItemPojo, OrderItemPojo newOrderItemPojo) throws ApiException {
        InventoryPojo inventoryPojo = inventoryService.getById(orderItemPojo.getProductId());
        int quantity = inventoryPojo.getQuantity() + orderItemPojo.getQuantity() - newOrderItemPojo.getQuantity();
        if(quantity<0){
            ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
            throw new ApiException("Insufficient inventory for barcode "+productPojo.getBarcode());
        }
        inventoryPojo.setQuantity(quantity);
        inventoryService.update(inventoryPojo);
    }

    private boolean checkInvoice(int orderId){
        OrderPojo orderPojo = getOnlyOrderById(orderId);
        return orderPojo.getInvoiceGenerated();
    }
    private void checkSellingPrice(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        for(OrderItemPojo orderItemPojo : orderItemPojoList){
            ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
            if(productPojo.getMrp()<orderItemPojo.getSellingPrice()){
                throw new ApiException("Selling price can't exceed MRP for barcode -"+productPojo.getBarcode());
            }
        }
    }

}
