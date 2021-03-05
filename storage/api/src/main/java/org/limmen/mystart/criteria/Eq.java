package org.limmen.mystart.criteria;

import java.util.stream.Stream;

import lombok.ToString;

@ToString(callSuper = true)
public class Eq extends AbstractFieldCriteria {

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

  @Override
  public Stream<AbstractFieldCriteria> toArguments() {
    return Stream.of(this);
  }    
}
