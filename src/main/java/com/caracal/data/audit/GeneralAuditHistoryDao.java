package com.caracal.data.audit;

import com.caracal.data.impl.AbstractDAOImpl;

import java.util.List;

public abstract class GeneralAuditHistoryDao extends AbstractDAOImpl<AuditHistory, Long> {

    public GeneralAuditHistoryDao() {
        super(AuditHistory.class);
    }

    public List<AuditHistory> getHistory(String entityName, Long id) {
        return getEntityManager().createNativeQuery("select * from audit_history" +
                " where entity_type = ? and JSON_EXTRACT(value , \"$.id\") = ?", AuditHistory.class)
                .setParameter(1, entityName)
                .setParameter(2, id)
                .getResultList();
    }

    @Override
    public String getCurrentUser() {
        throw new RuntimeException("Not implemented");
    }
}
