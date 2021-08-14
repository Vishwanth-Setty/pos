package com.increff.pos.dao;

import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDao {

    private static final String SELECT_ALL = "select p from OrderPojo p";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    OrderItemService orderItemService;

    public int insert(OrderPojo orderPojo){
        em.persist(orderPojo);
        em.flush();
        return orderPojo.getId();
    }
    public List<OrderPojo> selectAll(){
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public List<OrderItemPojo> select(int id){
        return orderItemService.getByOrderId(id);
    }

    protected TypedQuery<OrderPojo> getQuery(String jpql) {
        return em.createQuery(jpql, OrderPojo.class);
    }

}
