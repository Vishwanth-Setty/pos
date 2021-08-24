package com.increff.pos.service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemService {
    @Autowired
    OrderItemDao orderItemDao;
//
//    public List<OrderItemPojo> getByOrderId(int orderId){
//        return orderItemDao.selectByMethod("orderId",orderId);
//    }
//
//    public void create(OrderItemPojo orderItemPojo){
//        orderItemDao.persist(orderItemPojo);
//    }
//
//    public OrderItemPojo selectById(int id){
//        return orderItemDao.select(id);
//    }
}
