package com.aef3.data.audit;

import com.aef3.data.api.DomainDto;

import java.util.Date;

public class AuditHistoryDto implements DomainDto<AuditHistory, AuditHistoryDto> {

    private Long id;
    private String creator;
    private Date creationDate;
    private String value;

    @Override
    public AuditHistory toEntity() {
        AuditHistory auditHistory = new AuditHistory();
        auditHistory.setId(this.getId());
        auditHistory.setCreationDate(this.getCreationDate());
        auditHistory.setCreator(this.getCreator());
        auditHistory.setValue(this.getValue());
        return auditHistory;
    }

    @Override
    public AuditHistoryDto getInstance(AuditHistory auditHistory) {
        return AuditHistoryDto.toDto(auditHistory);
    }

    @Override
    public AuditHistoryDto getInstance() {
        return new AuditHistoryDto();
    }

    public static AuditHistory toEntity(AuditHistoryDto dto) {
        if(dto == null)
            return null;
        AuditHistory au = new AuditHistory();
        au.setValue(dto.getValue());
        au.setCreator(dto.getCreator());
        au.setCreationDate(dto.getCreationDate());
        au.setId(dto.getId());
        return au;
    }

    public static AuditHistoryDto toDto(AuditHistory entity) {
        if(entity == null)
            return null;
        AuditHistoryDto au = new AuditHistoryDto();
        au.setValue(entity.getValue());
        au.setCreator(entity.getCreator());
        au.setCreationDate(entity.getCreationDate());
        au.setId(entity.getId());
        return au;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
