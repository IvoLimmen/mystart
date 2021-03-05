package org.limmen.mystart.criteria;

import java.util.stream.Stream;

import lombok.ToString;

@ToString
public class And implements Criteria {
    
    private Criteria left;

    private Criteria right;

    public And(Criteria left, Criteria right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toSQL() {
        return "(" + left.toSQL() + " and " + right.toSQL() + ")";
    }

    @Override
    public Stream<AbstractFieldCriteria> toArguments() {
        return Stream.concat(left.toArguments(), right.toArguments());
    }  
}
