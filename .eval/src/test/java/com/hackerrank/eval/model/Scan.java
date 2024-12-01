package com.hackerrank.eval.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scan implements Serializable {
  boolean deleted = false;
  private Long id;
  private String domainName;
  private Integer numPages;
  private Integer numBrokenLinks;
  private Integer numMissingImages;
}
