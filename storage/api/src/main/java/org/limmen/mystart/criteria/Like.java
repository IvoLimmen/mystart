package org.limmen.mystart.criteria;

import java.util.stream.Stream;

import lombok.ToString;

@ToString(callSuper = true)
public class Like extends AbstractFieldCriteria {

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

  @Override
  public Stream<AbstractFieldCriteria> toArguments() {
    return Stream.of(this);
  }
}
