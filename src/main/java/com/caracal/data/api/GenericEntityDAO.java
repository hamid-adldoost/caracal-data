package com.caracal.data.api;


import java.io.Serializable;

/**
 * Generic Data access object interface.
 *
 * @param <E>  Type of the persistent entity
 * @param <PK> Type of the primary key of the persistent entity
 * @author Hamid
 * @version 1.0
 */

public interface GenericEntityDAO<E extends DomainEntity, PK extends Serializable> extends EntityOperator<E, PK> {


    public default String getCurrentUser(){
        return "Anonymous";
    }

}
