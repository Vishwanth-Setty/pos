package com.increff.pos.dao;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao<OrderItemPojo> {

    private static final String SELECT_BY_ORDER = "select p from OrderItemPojo p where orderId=:orderId";


    @PersistenceContext
    private EntityManager em;

    OrderItemDao() {
        super(OrderItemPojo.class);
    }

    public List<OrderItemPojo> getAllByOrderId(int orderId){
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDER);
        query.setParameter("orderId",orderId);

        return query.getResultList();
    }

//    protected TypedQuery<OrderItemPojo> getQuery(String jpql) {
//        return em.createQuery(jpql, OrderItemPojo.class);
//    }

}
