package org.limmen.mystart;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class LinkTest {

  @Test
  public void testSomeMethod() {

    Link google = new Link();
    google.setUrl("https://drive.google.com/a/qsd.nl/?tab=mo#my-drive");

    assertEquals("drive.google.com", google.getHost());
  }


  @Test
  public void testLastVisitedYears() {

    Link link = new Link();

    link.setLastVisit(LocalDateTime.now().minusYears(2));
    assertEquals("2 years ago", link.getFormattedLastVisit());    
  }

  @Test
  public void testLastVisitedMonths() {

    Link link = new Link();

    link.setLastVisit(LocalDateTime.now().minusMonths(2));
    assertEquals("2 months ago", link.getFormattedLastVisit());    

    link.setLastVisit(LocalDateTime.now().minusMonths(1));
    assertEquals("last month", link.getFormattedLastVisit());    
  }

  @Test
  public void testLastVisitedWeeks() {

    Link link = new Link();
    
    link.setLastVisit(LocalDateTime.now().minusDays(15));
    assertEquals("more than 2 weeks ago", link.getFormattedLastVisit());    

    link.setLastVisit(LocalDateTime.now().minusDays(8));
    assertEquals("more than a week ago", link.getFormattedLastVisit());    

    link.setLastVisit(LocalDateTime.now().minusDays(3));
    assertEquals("last week", link.getFormattedLastVisit());    
  }

  @Test
  public void testLastVisitedDays() {

    Link link = new Link();
    
    link.setLastVisit(LocalDateTime.now().minusDays(1));
    assertEquals("yesterday", link.getFormattedLastVisit());    

    link.setLastVisit(LocalDateTime.now());
    assertEquals("today", link.getFormattedLastVisit());    
  }
}
