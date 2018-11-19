package org.limmen.mystart.server.servlet.ajax;

import java.util.ArrayList;
import java.util.List;

public class ColorPicker {

  private final List<String> colors = new ArrayList<>();
  private int index = 0;

  public ColorPicker() {
    colors.add("#F15854");
    colors.add("#DECF3F");
    colors.add("#B276B2");
    colors.add("#B2912F");
    colors.add("#F17CB0");
    colors.add("#60BD68");
    colors.add("#FAA43A");
    colors.add("#5DA5DA");
    colors.add("#4D4D4D");
  }

  public String next() {
    if (index >= colors.size()) {
      index = 0;
    }
    return colors.get(index++);
  }
}
