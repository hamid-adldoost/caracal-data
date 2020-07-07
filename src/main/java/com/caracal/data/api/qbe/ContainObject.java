package com.caracal.data.api.qbe;

import java.util.List;

public class ContainObject {

    private String fieldName;
    private List<Object> ValueList;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<Object> getValueList() {
        return ValueList;
    }

    public void setValueList(List<Object> valueList) {
        this.ValueList = valueList;
    }
}
