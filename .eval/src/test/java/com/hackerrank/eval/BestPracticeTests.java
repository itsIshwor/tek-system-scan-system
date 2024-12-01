package com.hackerrank.eval;

import com.hackerrank.eval.extensions.RESTExtension;
import com.hackerrank.eval.fixtures.ScanDataFixtures;
import com.hackerrank.eval.model.Scan;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith({RESTExtension.class})
class BestPracticeTests {

  //Best practices tests
  @Test
  void whenScanRequestedThenCorrectOneIsReturned() {
    Scan scan = ScanDataFixtures.createAnyScan();
    get("/scan/" + scan.getId())
            .then()
            .statusCode(SC_OK)
            .body("id", equalTo(scan.getId().intValue()));
  }

  @Test
  void whenRequestingDeletionScanIsSoftDeleted() {
    Scan newScan = ScanDataFixtures.createAnyScan();
    Optional<Scan> optionalApiScan = getScanById(newScan.getId());
    assertThat(optionalApiScan.isPresent(), is(true));

    newScan = optionalApiScan.get();
    assertThat(newScan.isDeleted(), is(false));

    delete("/scan/" + newScan.getId());

    // It's possible that the Scan is returned with property deleted = true, or that it now returns
    // a 404 - no Scan
    getScanById(newScan.getId()).ifPresent(Scan -> assertThat(Scan.isDeleted(), is(true)));
  }

  @Test
  void whenRequestingADeletedScanThen404IsReturned() {
    Scan newScan = ScanDataFixtures.createAnyScan();
    get("/scan/" + newScan.getId()).then().statusCode(SC_OK);

    delete("/scan/" + newScan.getId());

    get("/scan/" + newScan.getId()).then().statusCode(SC_NOT_FOUND);
  }

  @Test
  void deletedScanNotPresentWhenRequestingAllScan() {
    whenRequestingDeletionScanIsSoftDeleted();
    Scan newScan = ScanDataFixtures.createAnyScan();
    assertScanPresent(true, newScan);

    delete("/scan/" + newScan.getId());

    assertScanPresent(false, newScan);
  }

  @Test
  void statusCode404WhenNonExistentScanRequested() {
    whenScanRequestedThenCorrectOneIsReturned();
    get("/scan/-1").then().statusCode(SC_NOT_FOUND);
  }

  @Test
  void statusCodeSuccessWhenDeletingScan() {
    Scan newScan = ScanDataFixtures.createAnyScan();
    delete("/scan/" + newScan.getId()).then().statusCode(anyOf(is(SC_OK), is(SC_NO_CONTENT)));
  }

  @Test
  void statusCode404WhenTryingToDeleteNonExistentScan() {
    whenRequestingDeletionScanIsSoftDeleted();
    delete("/scan/-1").then().statusCode(SC_NOT_FOUND);
  }

  @Test
  void statusCode400WhenSortByInvalid() {
    get("/scan/search/domain1.com?sortBy=invalid").then().statusCode(SC_BAD_REQUEST);
  }

  //get by id
  private Optional<Scan> getScanById(Long id) {
    Scan Scan = get("/scan/" + id).thenReturn().as(Scan.class);
    if (Scan.getId() == null) {
      return Optional.empty();
    } else {
      return Optional.of(Scan);
    }
  }

  //get all
  private void assertScanPresent(boolean expected, Scan newScan) {
    Scan[] allScan = get("/scan").andReturn().as(Scan[].class);
    assertThat(
            Stream.of(allScan).anyMatch(Scan -> Scan.getId().equals(newScan.getId())),
            is(expected));
  }
}
