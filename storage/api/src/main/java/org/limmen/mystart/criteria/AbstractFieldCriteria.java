package org.limmen.mystart.criteria;

import lombok.ToString;

@ToString
public abstract class AbstractFieldCriteria implements Criteria {

    private final String fieldName;

    private final Object value;

    private final Class<?> valueType;

    public AbstractFieldCriteria(String fieldName, Object value, Class<?> valueType) {
        this.fieldName = fieldName;
        this.value = value;
        this.valueType = valueType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getValueType() {
        return valueType;
    }
}
