package com.aef3.data.api.qbe;

/**
 * @author Adldoost
 * @version 1.0
 * @since it-1
 */
public class SortObject {

    String fieldName;
    SortOrder sortOrder;

    public SortObject(String fieldName, SortOrder sortOrder) {
        this.fieldName = fieldName;
        this.sortOrder = sortOrder;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }
}
