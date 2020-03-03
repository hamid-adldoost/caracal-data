package com.aef3.data.audit;

import com.aef3.data.impl.AbstractDAOImpl;

import java.util.List;

public abstract class GeneralAuditHistoryDao extends AbstractDAOImpl<AuditHistory, Long> {

    public GeneralAuditHistoryDao() {
        super(AuditHistory.class);
    }

    public List<AuditHistory> getHistory(String entityName, Long id) {
        return getEntityManager().createQuery("select e from AuditHistory e " +
                "where e.entityName = :entityName and id = :id", AuditHistory.class)
                .setParameter("entityName", entityName)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public String getCurrentUser() {
        throw new RuntimeException("Not implemented");
    }
}
