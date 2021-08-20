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
    private static final String SELECT = "select p from OrderPojo p where id=:id";
    private static final String SELECT_BY_INVOICE = "select p from OrderPojo p where invoiceGenerated=:invoiceGenerated";


    @PersistenceContext
    private EntityManager em;

    @Autowired
    OrderItemService orderItemService;

    public int insert(OrderPojo orderPojo){
        em.persist(orderPojo);
        return orderPojo.getId();
    }
    public List<OrderPojo> selectAll(){
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL);
        return query.getResultList();
    }

    public OrderPojo selectById(int id){
        TypedQuery<OrderPojo> query = getQuery(SELECT);
        query.setParameter("id",id);
        return query.getResultList().stream().findFirst().orElse(null);
    }
    public List<OrderItemPojo> select(int id){
        return orderItemService.getByOrderId(id);
    }

    public List<OrderPojo> selectAllWithInvoiceGenerated(boolean invoiceGenerated){
        TypedQuery<OrderPojo> query = getQuery(SELECT_BY_INVOICE);
        query.setParameter("invoiceGenerated",invoiceGenerated);
        return query.getResultList();
    }

    protected TypedQuery<OrderPojo> getQuery(String jpql) {
        return em.createQuery(jpql, OrderPojo.class);
    }

}
