package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao<BrandPojo> {

    @PersistenceContext
    private EntityManager em;

    private static final String SELECT_BY_NAME_AND_CATEGORY = "select p from BrandPojo p where brand=:brand and category=:category";

    BrandDao() {
        super(BrandPojo.class);
    }

    public BrandPojo selectByNameAndCategory(String brand,String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_NAME_AND_CATEGORY);
        query.setParameter("brand", brand);
        query.setParameter("category", category);

        return query.getResultList()
                .stream().findFirst().orElse(null);
    }

//    private TypedQuery<BrandPojo> getQuery(String jpql) {
//        return em.createQuery(jpql, BrandPojo.class);
//    }

}
