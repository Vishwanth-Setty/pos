package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao<BrandPojo> {

    @PersistenceContext
    private EntityManager em;

    private static final String delete_id = "delete from BrandPojo p where id=:id";
    private static final String select_id = "select p from BrandPojo p where id=:id";
    private static final String select_all = "select p from BrandPojo p";
    private static final String select_name_category = "select p from BrandPojo p where brand=:brand and category=:category";


    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(select_all);
        return query.getResultList();
    }

    public BrandPojo select(int id) {
        TypedQuery<BrandPojo> query = getQuery(select_id);
        query.setParameter("id", id);
        BrandPojo brandPojo = query.getResultList()
                .stream().findFirst().orElse(null);
        return brandPojo;
    }

    public int delete(int id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public BrandPojo selectByNameAndCategory(String brand,String category) {
        TypedQuery<BrandPojo> query = getQuery(select_name_category);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        BrandPojo brandPojo = query.getResultList()
                .stream().findFirst().orElse(null);
        return brandPojo;
    }

    TypedQuery<BrandPojo> getQuery(String jpql) {
        return em.createQuery(jpql, BrandPojo.class);
    }

}
