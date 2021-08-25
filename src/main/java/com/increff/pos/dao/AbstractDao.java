package com.increff.pos.dao;

import com.increff.pos.pojo.InventoryPojo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractDao<T> {

    @PersistenceContext
    protected EntityManager em;

    public Class<T> clazz;

    AbstractDao(Class<T> clazz1){
        clazz = clazz1;
    }

    public T persist(T t){
        em.persist(t);
        return t;
    }

    public  List<T> selectAll(){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);

        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    public T select(int id){
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), id ));
        criteriaQuery.select(root);

        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);

        return singleResult(typedQuery);
    }

    public <Z> T selectByMethod(String method, Z value) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.where(criteriaBuilder.equal(root.get(method), value));
        criteriaQuery.select(root);

        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);
        return singleResult(typedQuery);

    }
    public <Z> List<T> selectMultipleByMethod(String method, Z value) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.where(criteriaBuilder.equal(root.get(method), value));
        criteriaQuery.select(root);

        TypedQuery<T> typedQuery = em.createQuery(criteriaQuery);

        return typedQuery.getResultList();
    }

    protected TypedQuery<T> getQuery(String jpql) {
        return em.createQuery(jpql, clazz);
    }

    private T singleResult(TypedQuery<T> typedQuery){
        try{
            return typedQuery.getSingleResult();
        }
        catch (Exception e){
            return null;
        }
    }

}
