package com.increff.pos.service;

import com.increff.pos.pojo.OrderItemPojo;

public class CopyUtil {
    public OrderItemPojo copy(OrderItemPojo source,OrderItemPojo destination){
        destination.setOrderId(source.getOrderId());
        destination.setQuantity(source.getQuantity());
        destination.setSellingPrice(source.getSellingPrice());
        destination.setProductId(source.getProductId());
        return destination;
    }
}


