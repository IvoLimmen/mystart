package org.limmen.mystart.criteria;

import java.util.stream.Stream;

public class Or implements Criteria {

    private Criteria left;

    private Criteria right;

    public Or(Criteria left, Criteria right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toSQL() {
        return "(" + left.toSQL() + " or " + right.toSQL() + ")";
    }

    @Override
    public Stream<AbstractFieldCriteria> toArguments() {
        return Stream.concat(left.toArguments(), right.toArguments());
    }

    @Override
    public String toString() {
      return "Or [left=" + left + ", right=" + right + "]";
    }  
}
