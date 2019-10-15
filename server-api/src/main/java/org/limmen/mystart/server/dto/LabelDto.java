package org.limmen.mystart.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LabelDto implements Comparable<LabelDto> {

  private String label;
  private Long count;

  @Override
  public int compareTo(LabelDto o) {
    return String.CASE_INSENSITIVE_ORDER.compare(this.label, o.getLabel());
  }
} 