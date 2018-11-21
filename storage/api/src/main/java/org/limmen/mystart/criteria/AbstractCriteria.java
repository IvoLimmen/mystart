package org.limmen.mystart.criteria;

public abstract class AbstractCriteria {

  private final String fieldName;

  private final Object value;

  private final Class<?> valueType;

  public AbstractCriteria(String fieldName, Object value, Class<?> valueType) {
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

  public abstract String toSQL();
}
