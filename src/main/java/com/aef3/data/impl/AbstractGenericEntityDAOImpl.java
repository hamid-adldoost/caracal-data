package com.aef3.data.impl;

import com.aef3.data.api.DomainEntity;
import com.aef3.data.api.qbe.CompareObject;
import com.aef3.data.api.qbe.RangeObject;
import com.aef3.data.api.qbe.SortObject;
import com.aef3.data.api.GenericEntityDAO;
import com.aef3.data.api.qbe.StringSearchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public abstract class AbstractGenericEntityDAOImpl<E extends DomainEntity, PK extends Serializable> implements GenericEntityDAO<E, PK> {

    protected Class<E> persistentClass;

    private Logger logger = LoggerFactory.getLogger(AbstractGenericEntityDAOImpl.class);

    
    protected abstract EntityManager getEntityManager();
    
    protected abstract EntityManagerFactory getEntityManagerFactory();
    
    // to be compliant with CDI specs
    protected AbstractGenericEntityDAOImpl() {
    }

    protected AbstractGenericEntityDAOImpl(Class<E> persistentClass) {
        this.persistentClass = persistentClass;
    }

    private static boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    private static boolean isEntity(Class<?> clazz) {
        return clazz.getAnnotation(Entity.class) != null;
    }

    @Override
    public E save(E entity) {
        E persistentEntity = getEntityManager().merge(entity);
        getEntityManager().flush();
        return persistentEntity;
    }

    @Override
    public void save(List<E> entities) {
        for (E entity : entities) {
            E persistentEntity = getEntityManager().merge(entity);
        }
        getEntityManager().flush();
    }

    @Override
    public void remove(PK id) {
        if (id != null) {
            E entity = getEntityManager().find(this.persistentClass, id);
            if (entity != null) {
                getEntityManager().remove(entity);
                getEntityManager().flush();
            }
        }
    }

    @Override
    public void bulkRemove(List<PK> idList) {
        idList.forEach(e->{
            E entity = getEntityManager().find(this.persistentClass, e);
            getEntityManager().remove(entity);
        });
        getEntityManager().flush();
    }


    protected void enableQueryCache(Query query) {
        enableQueryCache(query, null);
    }

    protected void enableQueryCache(Query query, String region) {
        // Current implementation is based on hibernate jpa provider.
        query.setHint("org.hibernate.cacheable", true);
        if (region != null && !region.trim().isEmpty()) {
            query.setHint("org.hibernate.cacheRegion", region);
        }
    }

    private boolean isHibernateAvailable() {
        boolean hibernateProviderActive = false;
        try {
            Class.forName("org.hibernate.cfg.AvailableSettings");
            hibernateProviderActive = true;
        } catch (ClassNotFoundException ignored) {
        }
        return hibernateProviderActive;
    }

    final int defaultPageSize = 100;
    final int defaultStartIndex = 0;

    @Override
    public List<E> findByExample(E example, List<SortObject> sortObjectList, int startIndex, int pageSize, StringSearchType searchType) {
        return findByExample(example, sortObjectList, startIndex, pageSize, searchType, null, null);
    }

    @Override
    public List<E> findByExample(E example, List<SortObject> sortObjectList,
                                 int startIndex, int pageSize,
                                 StringSearchType searchType, List<RangeObject> rangeObjectList,
                                 List<CompareObject> compareObjectList) {
        try {
            Object object = example;
            StringBuilder query = new StringBuilder("SELECT e from " + object.getClass().getName() + " e where 1 = 1");

            query = new StringBuilder(insertEntityFieldsIntoQuery(query.toString(), example, "e."));

            if(rangeObjectList != null && !rangeObjectList.isEmpty()) {
                for (RangeObject r : rangeObjectList) {
                    query.append(" and " + r.getFieldName() + " >= :" + r.getFieldName() + "lowRange");
                    query.append(" and " + r.getFieldName() + " <= :" + r.getFieldName() + "highRange");
                }
            }

            if (compareObjectList != null && !compareObjectList.isEmpty()) {
                for(int i = 0; i < compareObjectList.size(); ++i) {
                    CompareObject c = (CompareObject)compareObjectList.get(i);
                    query.append(" and " + c.getFieldName() + " " + c.getOperator().getSqlval() + " :" + c.makeFieldNameParam() + "_" + i);
                }
            }


            if (sortObjectList != null && !sortObjectList.isEmpty()) {
                query.append(" ORDER BY ");

                for (SortObject so : sortObjectList) {
                    query.append(" e.").append(so.getFieldName()).append(" ").append(so.getSortOrder().name()).append(" ,");
                }

                char[] arr = query.toString().toCharArray();
                if (arr[arr.length - 1] == ',') {
                    arr = Arrays.copyOf(arr, arr.length - 1);
                }
                query = new StringBuilder(String.valueOf(arr));
            }



            Query q = getEntityManager().createQuery(query.toString(), example.getClass());

            insertEntityValuesIntoQuery(q, example, searchType);

            if(rangeObjectList != null && !rangeObjectList.isEmpty()) {
                for (RangeObject r : rangeObjectList) {
                    q.setParameter(r.getFieldName() + "lowRange", r.getLowRange());
                    q.setParameter(r.getFieldName() + "highRange", r.getHighRange());
                }
            }

            if (compareObjectList != null && !compareObjectList.isEmpty()) {
                for(int i = 0; i < compareObjectList.size(); ++i) {
                    CompareObject c = (CompareObject)compareObjectList.get(i);
                    q.setParameter(c.makeFieldNameParam() + "_" + i, c.getTarget());
                }
            }

            return (List<E>)q.setFirstResult(startIndex).setMaxResults(pageSize).getResultList();

        } catch (Exception e) {

            logger.error("FindByExample unable to perform for example : " + example, e);
            throw new RuntimeException("FIND_BY_EXAMPLE_EXCEPTION");
//            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "{0} {1}", new Object[]{DAOExceptionCodes.EXP_DAO_ABS_007_MSG, e.getMessage()});
//            throw new DAOException(DAOExceptionCodes.EXP_DAO_ABS_007_MSG, e);
        }
    }

    @Override
    public List<E> findByExample(E example, int startIndex, int pageSize, StringSearchType searchType) {
        return findByExample(example, null, startIndex, pageSize, searchType);
    }

    @Override
    public List<E> findByExample(E example, int startIndex, int pageSize, StringSearchType searchType, List<RangeObject> rangeObjectList) {
        return findByExample(example, null, startIndex, pageSize, searchType, rangeObjectList, null);
    }

    @Override
    public List<E> findByExample(E example) {
        return findByExample(example, null, defaultStartIndex, defaultPageSize, StringSearchType.EXPAND_BOTH_SIDES);
    }

    @Override
    public List<E> findByExample(E example, List<SortObject> sortObjectList) {
        return findByExample(example, sortObjectList, defaultStartIndex, defaultPageSize, StringSearchType.EXPAND_BOTH_SIDES);
    }

    @Override
    public List<E> findByExample(E example, List<SortObject> sortObjectList, List<RangeObject> rangeObjectList) {
        return findByExample(example, sortObjectList, defaultStartIndex, defaultPageSize, StringSearchType.EXPAND_BOTH_SIDES, rangeObjectList, null);
    }

    @Override
    public List<E> findByExample(E example, List<SortObject> sortObjectList, StringSearchType searchType, List<RangeObject> rangeObjectList) {
        return findByExample(example, sortObjectList, defaultStartIndex, defaultPageSize, searchType, rangeObjectList, null);
    }

    @Override
    public List<E> findByExample(E example, List<SortObject> sortObjectList, StringSearchType searchType) {
        return findByExample(example, sortObjectList, defaultStartIndex, defaultPageSize, searchType);
    }

    @Override
    public List<E> findByExample(E example, StringSearchType searchType) {
        return findByExample(example, null, defaultStartIndex, defaultPageSize, searchType);
    }

    @Override
    public E findSingleByExample(E example, List<SortObject> sortObjectList, StringSearchType searchType) {
        List<E> list = findByExample(example, sortObjectList, 0, 1, searchType);
        if(list == null || list.isEmpty())
            return  null;
        else
            return list.get(0);
    }

    @Override
    public E findSingleByExample(E example, List<SortObject> sortObjectList) {
        return findSingleByExample(example, sortObjectList, StringSearchType.EXPAND_BOTH_SIDES);
    }

    @Override
    public E findSingleByExample(E example, StringSearchType searchType) {
        return findSingleByExample(example, null, searchType);
    }

    @Override
    public E findSingleByExample(E example) {
        return findSingleByExample(example, null, null);
    }

    private String insertEntityFieldsIntoQuery(String query, DomainEntity entity, String prefix) throws Exception {

        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                Object fieldValue;
                field.setAccessible(true);
                fieldValue = field.get(entity);
                if (fieldValue == null)
                    continue;
                else if (field.get(entity) instanceof List || (java.lang.reflect.Modifier.isStatic(field.getModifiers())) || field.getName().startsWith("_")) {
                    continue;
                } else if (field.get(entity) instanceof DomainEntity) {
                    query = insertEntityFieldsIntoQuery(query, (DomainEntity) field.get(entity), prefix + field.getName() + ".");
                } else {
                    if(field.getAnnotation(Id.class) != null){
                        query += " and " + prefix + field.getName() + " = :" +  entity.getClass().getSimpleName() + field.getName();
                        break;
                    }
                    else if ((fieldValue instanceof String) && !((String) fieldValue).isEmpty()) {
                        query += " and " + prefix + field.getName() + " LIKE :" + entity.getClass().getSimpleName() + field.getName();
                    } else if (!(fieldValue instanceof String)) {
                        query += " and " + prefix + field.getName() + " = :" + entity.getClass().getSimpleName() + field.getName();
                    }
                }
            }
            return query;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw e;
        }
    }

    private Query insertEntityValuesIntoQuery(Query query, DomainEntity entity, StringSearchType searchType) throws Exception {

        try {
            Field[] fields = entity.getClass().getDeclaredFields();

            for (Field field : fields) {
                Object fieldValue;
                field.setAccessible(true);
                fieldValue = field.get(entity);
                if (fieldValue == null) {
                    continue;
                }
                if (field.get(entity) instanceof List || (java.lang.reflect.Modifier.isStatic(field.getModifiers())) || field.getName().startsWith("_")) {
                    continue;
                } else if (field.get(entity) instanceof DomainEntity) {
                    insertEntityValuesIntoQuery(query, (DomainEntity) field.get(entity), searchType);
                } else {
                    if(field.getAnnotation(Id.class) != null){
                        query.setParameter( entity.getClass().getSimpleName() + field.getName(), field.get(entity));
                        break;
                    }
                    else if ((fieldValue instanceof String) && !((String) fieldValue).isEmpty()) {
                        if (searchType == StringSearchType.EXPAND_BOTH_SIDES || searchType == null)
                            query.setParameter( entity.getClass().getSimpleName() + field.getName(), "%" + field.get(entity) + "%");
                        else if (searchType == StringSearchType.EXPAND_LEFT)
                            query.setParameter(entity.getClass().getSimpleName() + field.getName(), "%" + field.get(entity));
                        else if (searchType == StringSearchType.EXPAND_RIGHT)
                            query.setParameter(entity.getClass().getSimpleName() + field.getName(), field.get(entity) + "%");
                        else if (searchType == StringSearchType.EXACT)
                            query.setParameter(entity.getClass().getSimpleName() + field.getName(), field.get(entity));
                    } else if (!(fieldValue instanceof String)) {
                        query.setParameter(entity.getClass().getSimpleName() + field.getName(), field.get(entity));
                    }
                }
            }
            return query;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public long countByExample(E example, StringSearchType searchType) {
        try{
            Object object = example;
            StringBuilder query = new StringBuilder("select count(e) from " + object.getClass().getName() + " e where 1 = 1");

            query = new StringBuilder(insertEntityFieldsIntoQuery(query.toString(), example, "e."));

            Query q = getEntityManager().createQuery(query.toString(), Long.class);

            insertEntityValuesIntoQuery(q, example, searchType);

            return (long)q.getSingleResult();
        }
        catch(Exception e){
            logger.error("CountByExample unable to perform for example : " + example, e);
            throw new RuntimeException("CountByExampleException");
        }
    }

    @Override
    public void removeByExample(E example, StringSearchType searchType) {
        List<E> list = findByExample(example, 0, Integer.MAX_VALUE, searchType);
        for(E e : list){
            getEntityManager().remove(e);
        }
        getEntityManager().flush();
    }

    @Override
    public E findByPrimaryKey(PK primaryKey) {
        return getEntityManager().find(persistentClass, primaryKey);
    }

    public long countByExample(E example, StringSearchType searchType, List<RangeObject> rangeObjectList, List<CompareObject> compareObjectList) {
        try {
            StringBuilder query = new StringBuilder("select count(e) from " + example.getClass().getName() + " e where 1 = 1");
            query = new StringBuilder(this.insertEntityFieldsIntoQuery(query.toString(), example, "e."));
            if (rangeObjectList != null && !rangeObjectList.isEmpty()) {
                Iterator var7 = rangeObjectList.iterator();

                while(var7.hasNext()) {
                    RangeObject r = (RangeObject)var7.next();
                    query.append(" and " + r.getFieldName() + " >= :" + r.getFieldName() + "lowRange");
                    query.append(" and " + r.getFieldName() + " <= :" + r.getFieldName() + "highRange");
                }
            }

            if (compareObjectList != null && !compareObjectList.isEmpty()) {
                for(int i = 0; i < compareObjectList.size(); ++i) {
                    CompareObject c = (CompareObject)compareObjectList.get(i);
                    query.append(" and " + c.getFieldName() + " " + c.getOperator().getSqlval() + " :" + c.makeFieldNameParam() + "_" + i);
                }
            }

            Query q = this.getEntityManager().createQuery(query.toString(), Long.class);
            this.insertEntityValuesIntoQuery(q, example, searchType);
            if (rangeObjectList != null && !rangeObjectList.isEmpty()) {
                Iterator var14 = rangeObjectList.iterator();

                while(var14.hasNext()) {
                    RangeObject r = (RangeObject)var14.next();
                    q.setParameter(r.getFieldName() + "lowRange", r.getLowRange());
                    q.setParameter(r.getFieldName() + "highRange", r.getHighRange());
                }
            }

            if (compareObjectList != null && !compareObjectList.isEmpty()) {
                for(int i = 0; i < compareObjectList.size(); ++i) {
                    CompareObject c = (CompareObject)compareObjectList.get(i);
                    q.setParameter(c.makeFieldNameParam() + "_" + i, c.getTarget());
                }
            }

            return (Long)q.getSingleResult();
        } catch (Exception var10) {
            this.logger.error("CountByExample unable to perform for example : " + example, var10);
            throw new RuntimeException("CountByExampleException");
        }
    }
}
