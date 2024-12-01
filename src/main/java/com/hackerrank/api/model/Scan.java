package com.hackerrank.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Scan implements Serializable {
  private boolean deleted = false;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "domain_name")
  private String domainName;
  @Column(name = "num_pages")
  private Integer numPages;
  @Column(name = "num_broken_links")
  private Integer numBrokenLinks;
  @Column(name = "num_missing_images")
  private Integer numMissingImages;
}
