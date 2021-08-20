package com.increff.pos.dao;

import org.hibernate.type.EntityType;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractDao<T> {

    @PersistenceContext
    private EntityManager em;

    public <T> void insert(T t){
        em.persist(t);
    }
//    public <T> List<T> selectAll(EntityType type){
//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(type.getEntityClass());
//        Root<T> root = criteriaQuery.from(T.class);
//        criteriaQuery.select(root);
//        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);
//        return typedQuery.getResultList();
//
//    }
//    public <T> T select(int id){
//
//    }

}
