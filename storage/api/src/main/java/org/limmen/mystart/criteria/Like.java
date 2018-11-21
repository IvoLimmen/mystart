package org.limmen.mystart.criteria;

public class Like extends AbstractCriteria {

  public Like(String fieldName, Object value, Class<?> valueType) {
    super(fieldName, value, valueType);
  }

  @Override
  public String toSQL() {
    if (String.class.equals(getValueType())) {
      return getFieldName() + " ilike ?";
    } else if (String[].class.equals(getValueType())) {
      return "? ilike any(" + getFieldName() + ")";
    }
    return null;
  }
}
