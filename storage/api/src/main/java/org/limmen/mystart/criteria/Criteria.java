package org.limmen.mystart.criteria;

import java.util.stream.Stream;

public interface Criteria {

  public String toSQL();

  public Stream<AbstractFieldCriteria> toArguments();
}
