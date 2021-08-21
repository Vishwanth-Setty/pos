package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class BrandDao extends AbstractDao<BrandPojo> {

    @PersistenceContext
    private EntityManager em;



    private static final String SELECT_BY_ID = "select p from BrandPojo p where id=:id";
    private static final String SELECT_ALL = "select p from BrandPojo p";
    private static final String SELECT_BY_NAME_AND_CATEGORY = "select p from BrandPojo p where brand=:brand and category=:category";

    BrandDao(Class<BrandPojo> clazz1) {
        super(clazz1);
    }

//    public List<BrandPojo> selectAll() {
//        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL);
//        return query.getResultList();
//    }
//
//    public BrandPojo select(int id) {
//        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_ID);
//        query.setParameter("id", id);
//        BrandPojo brandPojo = query.getResultList()
//                .stream().findFirst().orElse(null);
//        return brandPojo;
//    }

    public BrandPojo selectByNameAndCategory(String brand,String category) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_NAME_AND_CATEGORY);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        BrandPojo brandPojo = query.getResultList()
                .stream().findFirst().orElse(null);
        return brandPojo;
    }

    private TypedQuery<BrandPojo> getQuery(String jpql) {
        return em.createQuery(jpql, BrandPojo.class);
    }

}
