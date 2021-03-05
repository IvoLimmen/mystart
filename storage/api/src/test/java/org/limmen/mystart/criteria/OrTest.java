package org.limmen.mystart.criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class OrTest {

    @Test
    public void createOr() {
    
        Eq field1 = new Eq("description", "test", String.class);
        Eq field2 = new Eq("title", "test", String.class);

        Criteria test = new Or(field1, field2);

        assertEquals("(description = ? or title = ?)", test.toSQL());         


        var expected = List.of(field1, field2);

        assertEquals(expected, test.toArguments().collect(Collectors.toList()));
    }
}
