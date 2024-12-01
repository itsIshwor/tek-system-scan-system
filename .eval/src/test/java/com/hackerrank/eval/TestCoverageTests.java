package com.hackerrank.eval;

import com.hackerrank.eval.evaluation.reports.TestCoverageCalculator;
import com.hackerrank.eval.evaluation.reports.TestCoverageReport;
import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
class TestCoverageTests {
  private static TestCoverageReport report;

  @BeforeAll
  static void setup() {
    report = TestCoverageCalculator.getStatsOfTestsRun();
  }

  private void testsAreAllPassing() {
    assertThat("Reports failed", report.getFailed(), equalTo(0));
    assertThat("Reports errored", report.getErrored(), equalTo(0));
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3, 4, 5})
  void wroteAtLeastXNumberOfTests(int number) {
    testsAreAllPassing();
    assertThat("Tests run", report.getRun(), greaterThanOrEqualTo(number));
  }
}
