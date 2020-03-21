package org.limmen.mystart;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LinkTest {

  @Test
  public void testSomeMethod() {

    Link google = new Link();
    google.setUrl("https://drive.google.com/a/qsd.nl/?tab=mo#my-drive");

    assertEquals("drive.google.com", google.getHost());
  }

}
