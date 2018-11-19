package org.limmen.mystart;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DomainUtil {

  public static String formatLabels(Link link) {
    return link.getLabels().stream().collect(Collectors.joining(", "));
  }

  public static Set<String> parseLabels(String label) {
    String[] labels;
    if (label.contains(",")) {
      labels = label.split(",");
    } else {
      labels = new String[1];
      labels[0] = label;
    }

    return Arrays.asList(labels).stream()
        .map(l -> l.trim())
        .filter(l -> !l.isEmpty())
        .collect(Collectors.toSet());
  }
}
