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

    private static String select_id = "select p from InventoryPojo p where productId=:id";

    InventoryDao() {
        super(InventoryPojo.class);
    }

}
