package com.hackerrank.eval;

import com.hackerrank.eval.extensions.RESTExtension;
import com.hackerrank.eval.fixtures.ScanDataFixtures;
import com.hackerrank.eval.model.Scan;
import static io.restassured.RestAssured.get;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.emptyIterable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Arrays;
import java.util.List;

@ExtendWith({RESTExtension.class})
class FunctionalTests {

  @Test
  void whenRequestingVulnerabilitiesSortedThenExpectRightOrder() {
    List<Scan> scansSamples = ScanDataFixtures.getScanSamples();
    List<Scan> expectedList = ScanDataFixtures.getListOrderedByScore(scansSamples, "test-domain1.com");

    List<Scan> resultList =
            Arrays.asList(
                    get("/scan/search/test-domain1.com?orderBy=numPages")
                            .then()
                            .statusCode(SC_OK)
                            .extract()
                            .response()
                            .as(Scan[].class));
    System.out.println(resultList);
    System.out.println(expectedList);

    assertThat(
            resultList.stream().map(Scan::getId).toArray(),
            arrayContaining(expectedList.stream().map(Scan::getId).toArray()));
  }

  @Test
  void whenRequestingVulnerabilitiesSortedWithNoVulnerabilitiesExpectEmptyList() {
    get("/scan/search/-1?orderBy=numPages")
            .then()
            .statusCode(SC_OK)
            .body("$", emptyIterable());
  }
}
