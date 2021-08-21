package com.increff.pos.dao;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

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

    public Class<T> clazz;

    public  void insert(T t){
        em.persist(t);
    }
//    public  List<T> selectAll(){
//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
//        Root<T> root = criteriaQuery.from(clazz);
//        criteriaQuery.select(root);
//        System.out.println(criteriaQuery);
//        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);
//        return typedQuery.getResultList();
//    }
//    public T select(int id){
//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
//        Root<T> root = criteriaQuery.from(clazz);
//        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id ));
//        criteriaQuery.select(root);
//        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);
//        return typedQuery.getResultList().get(0);
//    }

}
