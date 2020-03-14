package org.limmen.mystart.server.dto;

import lombok.Data;

@Data
public class LinkDto {
  private Long id;
  private String url;
  private String description;
  private String title;
  private String[] labels;
}