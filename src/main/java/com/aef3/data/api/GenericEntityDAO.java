package com.aef3.data.api;


import com.aef3.data.api.qbe.CompareObject;
import com.aef3.data.api.qbe.ContainObject;
import com.aef3.data.api.qbe.RangeObject;
import com.aef3.data.api.qbe.StringSearchType;

import java.io.Serializable;
import java.util.List;

/**
 * Generic Data access object interface.
 *
 * @param <E>  Type of the persistent entity
 * @param <PK> Type of the primary key of the persistent entity
 * @author Hamid
 * @version 1.0
 */

public interface GenericEntityDAO<E extends DomainEntity, PK extends Serializable> extends EntityOperator<E, PK> {

}
