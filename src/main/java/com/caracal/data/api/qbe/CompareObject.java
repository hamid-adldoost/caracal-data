package com.caracal.data.api.qbe;

public class CompareObject {
    private String fieldName;
    private CompareObject.Operator operator;
    private Object target;

    public CompareObject() {
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String makeFieldNameParam() {
        return this.fieldName + "_" + this.operator.name();
    }

    public CompareObject.Operator getOperator() {
        return this.operator;
    }

    public void setOperator(CompareObject.Operator operator) {
        this.operator = operator;
    }

    public Object getTarget() {
        return this.target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public static CompareObject of(String fieldName, CompareObject.Operator operator, Object target) {
        CompareObject compareObject = new CompareObject();
        compareObject.fieldName = fieldName;
        compareObject.operator = operator;
        compareObject.target = target;
        return compareObject;
    }

    public static enum Operator {
        G(">"),
        GE(">="),
        EQ("="),
        LE("<="),
        L("<");

        private String sqlval;

        private Operator(String sqlval) {
            this.sqlval = sqlval;
        }

        public String getSqlval() {
            return this.sqlval;
        }
    }
}
