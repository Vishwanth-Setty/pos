package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao<ProductPojo> {
    @PersistenceContext
    private EntityManager em;

    private static String SELECT_BRAND_ID = "select p from ProductPojo p where brandId=:brandId";
    private static String SELECT_BY_BARCODE = "select p from ProductPojo p where barcode=:barcode";

    ProductDao() {
        super(ProductPojo.class);
    }

}
