/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aef3.data.impl;

import com.aef3.data.api.AbstractDAO;
import com.aef3.data.api.DomainEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 *
 * @author Hamid
 * @param <E>
 * @param <PK>
 */
public abstract class AbstractDAOImpl<E extends DomainEntity, PK extends Serializable> extends AbstractGenericEntityDAOImpl<E, PK> implements AbstractDAO<E, PK> {

    @PersistenceContext
    private EntityManager em;
    
    public AbstractDAOImpl() {
        super(null);
    }

    public AbstractDAOImpl(Class<E> entityClass) {

        super(entityClass);
    }

//    @PostConstruct
//    public void init() {
//        Logger.getLogger(this.getClass().getName()).log(Level.INFO, getEntityManagerFactory().toString());
//    }
    
    @Override
    protected EntityManager getEntityManager(){
        return em;
    }

    @Override
    protected EntityManagerFactory getEntityManagerFactory() {
        return getEntityManager().getEntityManagerFactory();
    }

//    @Override
//    public void create(E t) {
//        em.persist(t);
//    }
//
//    @Override
//    public void edit(E t) {
//        em.merge(t);
//    }
   
}
