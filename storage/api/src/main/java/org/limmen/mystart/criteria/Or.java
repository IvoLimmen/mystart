package org.limmen.mystart.criteria;

import java.util.List;
import java.util.stream.Stream;

import lombok.ToString;

@ToString
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
}
