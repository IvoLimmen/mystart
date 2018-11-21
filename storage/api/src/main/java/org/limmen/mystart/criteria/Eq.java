package org.limmen.mystart.criteria;

public class Eq extends AbstractCriteria {

  public Eq(String fieldName, Object value, Class<?> valueType) {
    super(fieldName, value, valueType);
  }

  @Override
  public String toSQL() {
    if (String.class.equals(getValueType())) {
      return getFieldName() + " = ?";
    } else if (String[].class.equals(getValueType())) {
      return "? = any(" + getFieldName() + ")";
    }
    return null;
  }
}
