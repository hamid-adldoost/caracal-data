/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aef3.data.api;

import java.io.Serializable;

/**
 *
 * @author Hamid
 * @param <E>
 * @param <PK>
 */
public interface AbstractDAO<E extends DomainEntity, PK extends Serializable> extends GenericEntityDAO<E, PK> {
    
}
