package org.limmen.mystart;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DomainUtil {

  public static String formatLabels(Link link) {
    return link.getLabels().stream().collect(Collectors.joining(", "));
  }

  public static Set<String> parseLabels(String label) {
    // remove the leading and trailing ;
    label = label.substring(1);
    label = label.substring(0, label.length() - 1);

    String[] labels;
    if (label.contains(",")) {
      labels = label.split(",");
    } else if (label.contains(";")) {
      labels = label.split(";");
    } else {
      labels = new String[1];
      labels[0] = label;
    }

    return Arrays.asList(labels).stream()
        .map(l -> l.trim())
        .collect(Collectors.toSet());
  }

  public static String storeLabels(Link link) {
    return ";" + link.getLabels().stream().collect(Collectors.joining(";")) + ";";
  }
}
