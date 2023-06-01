package org.limmen.mystart.server.servlet.model;

public class ChartData {

  public String label;
  public String color;
  public String value;
  public String highlight;
  
  public ChartData(String label, String color, String value, String highlight) {
    this.label = label;
    this.color = color;
    this.value = value;
    this.highlight = highlight;
  }
}
