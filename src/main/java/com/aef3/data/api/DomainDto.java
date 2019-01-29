package com.aef3.data.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface DomainDto<E extends DomainEntity, T extends DomainDto> extends Serializable {

    E toEntity();
    T getInstance(E entity);
    T getInstance();


    default  <E extends DomainEntity, T extends DomainDto> List<E> toEntity(List<T> dtoList) {
        if(dtoList == null || dtoList.isEmpty())
            return null;
        List<E> entityList = new ArrayList<>();

        dtoList.forEach(d -> {
            entityList.add((E)d.toEntity());
        });
        return entityList;
    }

    default List<T> getInstance(List<E> entityList) {

        if(entityList == null)
            return null;
        List<T> dtoList = new ArrayList<>();
        entityList.forEach(e -> {
            dtoList.add(getInstance(e));
        });
        return dtoList;
    }
}
