package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao {

    private static final String SELECT_BY_ORDER = "select p from OrderItemPojo p where orderId=:orderId";
    private static final String SELECT = "select p from OrderItemPojo p where id=:id";


    @PersistenceContext
    private EntityManager em;

    public List<OrderItemPojo> getAllByOrderId(int orderId){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDER);
        query.setParameter("orderId",orderId);
        return query.getResultList();
    }
    public void insert(OrderItemPojo orderItemPojo){
        em.persist(orderItemPojo);
    }
    public OrderItemPojo select(int id){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT);
        query.setParameter("id",id);
        return query.getResultList()
                .stream().findFirst().orElse(null);
    }
    protected TypedQuery<OrderItemPojo> getQuery(String jpql) {
        return em.createQuery(jpql, OrderItemPojo.class);
    }

}
