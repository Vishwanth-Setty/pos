package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao<OrderPojo> {

    private static final String SELECT_ALL = "select p from OrderPojo p";
    private static final String SELECT = "select p from OrderPojo p where id=:id";
    private static final String SELECT_BY_INVOICE = "select p from OrderPojo p where invoiceGenerated=:invoiceGenerated";


    @PersistenceContext
    private EntityManager em;

    @Autowired
    OrderItemDao orderItemDao;

    OrderDao() {
        super(OrderPojo.class);
    }

    public List<OrderItemPojo> selectOrderDetails(int id){
        return orderItemDao.selectMultipleByMethod("orderId",id);
    }

}
