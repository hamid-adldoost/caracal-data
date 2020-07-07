package com.caracal.data.api.qbe;

public class RangeObject {

    private String fieldName;
    private Object lowRange;
    private Object highRange;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getLowRange() {
        return lowRange;
    }

    public void setLowRange(Object lowRange) {
        this.lowRange = lowRange;
    }

    public Object getHighRange() {
        return highRange;
    }

    public void setHighRange(Object highRange) {
        this.highRange = highRange;
    }
}
