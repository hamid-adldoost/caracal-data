package com.aef3.data.api;


import com.aef3.data.api.qbe.*;

import java.io.Serializable;
import java.util.List;

public interface EntityOperator<E extends DomainEntity, PK extends Serializable> {

    E save(E entity);

    void save(List<E> entities);
    
//    void create(E t);
//
//    void edit(E t);

    void remove(PK id);


    void bulkRemove(List<PK> entityIdList);


    List<E> findByExample(E example, List<SortObject> sortObjectList, int startIndex, int pageSize, StringSearchType searchType);

    List<E> findByExample(E example, List<SortObject> sortObjectList, int startIndex, int pageSize,
                          StringSearchType searchType, List<RangeObject> rangeObjectList,
                          List<CompareObject> compareObjectList, List<ContainObject> containObjectList, String wherePhraseExt);

    List<E> findByExample(E example, List<SortObject> sortObjectList,
                                 int startIndex, int pageSize,
                                 StringSearchType searchType, List<RangeObject> rangeObjectList,
                                 List<CompareObject> compareObjectList);

    List<E> findByExample(E example, List<SortObject> sortObjectList,
                          int startIndex, int pageSize,
                          StringSearchType searchType, List<RangeObject> rangeObjectList,
                          List<CompareObject> compareObjectList, String wherePhraseExt);

    List<E> findByExample(E example, int startIndex, int pageSize, StringSearchType searchType, List<RangeObject> rangeObjectList);

    List<E> findByExample(E example, List<SortObject> sortObjectList, List<RangeObject> rangeObjectList);

    List<E> findByExample(E example, List<SortObject> sortObjectList, StringSearchType searchType, List<RangeObject> rangeObjectList);
    
    List<E> findByExample(E example, int startIndex, int pageSize, StringSearchType searchType);
    
    List<E> findByExample(E example);
    
    List<E> findByExample(E example, List<SortObject> sortObjectList);
    
    List<E> findByExample(E example, List<SortObject> sortObjectList, StringSearchType searchType);

    List<E> findByExample(E example, StringSearchType searchType);

    E findSingleByExample(E example, List<SortObject> sortObjectList, StringSearchType searchType);
    
    E findSingleByExample(E example, List<SortObject> sortObjectList);
    
    E findSingleByExample(E example, StringSearchType searchType);
    
    E findSingleByExample(E example);
    
    long countByExample(E example, StringSearchType searchType);

    long countByExample(E var1, StringSearchType var2, List<RangeObject> var3, List<CompareObject> var4);

    long countByExample(E var1, StringSearchType var2, List<RangeObject> var3, List<CompareObject> var4,
                        String wherePhraseExt);

    long countByExample(E example, StringSearchType searchType, List<RangeObject> rangeObjectList,
                        List<CompareObject> compareObjectList, List<ContainObject> containObjectList,
                        String wherePhraseExt);

    void removeByExample(E example, StringSearchType searchType);

    E findByPrimaryKey(PK primaryKey);

}
