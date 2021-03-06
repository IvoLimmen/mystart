package org.limmen.mystart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class DomainUtilTest {

  @Test
  public void parseLabelsSimple() {

    Set<String> values = DomainUtil.parseLabels("A,B,C");

    assertNotNull(values);
    assertEquals(3, values.size());
  }

  @Test
  public void parseLabelsComplex() {

    Set<String> values = DomainUtil.parseLabels("A,B , C, , D");

    assertNotNull(values);
    assertEquals(4, values.size());

    Iterator<String> it = values.iterator();

    assertEquals("A", it.next());
    assertEquals("B", it.next());
    assertEquals("C", it.next());
    assertEquals("D", it.next());
  }
}
