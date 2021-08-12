package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
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

    private static String delete_id = "delete from ProductPojo p where id=:id";
    private static String select_id = "select p from ProductPojo p where id=:id";
    private static String selectByBarcode = "select p from ProductPojo p where barcode=:barcode";
    private static String select_all = "select p from ProductPojo p";

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all);
        return query.getResultList();
    }

    public ProductPojo select(int id) {
        TypedQuery<ProductPojo> query = getQuery(select_id);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public ProductPojo selectByBarcode(String barcode){
        TypedQuery<ProductPojo> query = getQuery(selectByBarcode);
        query.setParameter("barcode", barcode);
        return query.getResultList()
                .stream().findFirst().orElse(null);
    }

    public int delete(int id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    TypedQuery<ProductPojo> getQuery(String jpql) {
        return em.createQuery(jpql, ProductPojo.class);
    }

}
