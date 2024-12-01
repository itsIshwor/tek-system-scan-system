package com.hackerrank.eval.fixtures;

import com.hackerrank.eval.model.Scan;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_CREATED;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScanDataFixtures {
  private ScanDataFixtures() {
  }

  public static Scan createAnyScan() {
    return createScan(Scan.builder().domainName("test.com").build());
  }

  //create
  public static Scan createScan(Scan scan) {
    return given()
            .request()
            .contentType(JSON)
            .body(scan)
            .post("/scan")
            .then()
            .statusCode(SC_CREATED)
            .extract()
            .response()
            .as(Scan.class);
  }

  public static List<Scan> getScanSamples() {

    return Arrays.asList(
            ScanDataFixtures.createScan(
                    Scan.builder()
                            .domainName("test-domain1.com")
                            .numPages(220)
                            .numBrokenLinks(22)
                            .numMissingImages(27)
                            .build()),
            ScanDataFixtures.createScan(
                    Scan.builder()
                            .domainName("test-domain1.com")
                            .numPages(720)
                            .numBrokenLinks(72)
                            .numMissingImages(77)
                            .build()),
            ScanDataFixtures.createScan(
                    Scan.builder()
                            .domainName("test-domain1.com")
                            .numPages(420)
                            .numBrokenLinks(24)
                            .numMissingImages(47)
                            .build()),
            ScanDataFixtures.createScan(
                    Scan.builder()
                            .domainName("domain2.com")
                            .numPages(120)
                            .numBrokenLinks(28)
                            .numMissingImages(67)
                            .build()),
            ScanDataFixtures.createScan(
                    Scan.builder()
                            .domainName("domain2.com")
                            .numPages(200)
                            .numBrokenLinks(20)
                            .numMissingImages(70)
                            .build()),
            ScanDataFixtures.createScan(
                    Scan.builder()
                            .domainName("domain3.com")
                            .numPages(60)
                            .numBrokenLinks(20)
                            .numMissingImages(17)
                            .build())

    );

  }

  public static List<Scan> getListOrderedByScore(List<Scan> source, String domainName) {
    List<Scan> copyOfSource = new ArrayList<>(source);
    copyOfSource = copyOfSource.stream().filter(v -> domainName.equals(v.getDomainName())).collect(Collectors.toList());
    copyOfSource.sort(Comparator.comparing(Scan::getNumPages));
    return copyOfSource;
  }
}
