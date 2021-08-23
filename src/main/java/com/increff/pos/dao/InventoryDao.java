package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao<InventoryPojo> {

    @PersistenceContext
    private EntityManager em;

    private static String delete_id = "delete from InventoryPojo p where productId=:id";
    private static String select_id = "select p from InventoryPojo p where productId=:id";
//    private static String select_all = "select p from InventoryPojo p";

    InventoryDao() {
        super(InventoryPojo.class);
    }


//    public List<InventoryPojo> selectAll(){
//        TypedQuery<InventoryPojo> query = getQuery(select_all);
//        return query.getResultList();
//    }
    public InventoryPojo select(int id){
        TypedQuery<InventoryPojo> query = getQuery(select_id);
        query.setParameter("id", id);
        return query.getResultList()
                .stream().findFirst().orElse(null);
    }


    TypedQuery<InventoryPojo> getQuery(String jpql) {
        return em.createQuery(jpql, InventoryPojo.class);
    }


}
