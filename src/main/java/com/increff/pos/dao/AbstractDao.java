package com.increff.pos.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class AbstractDao<T> {

    @PersistenceContext
    private EntityManager em;

    public void insert(T t){
        em.persist(t);
    }
    public abstract List<T> selectAll();
    public abstract T select(int id);
    public abstract int delete(int id);

}
