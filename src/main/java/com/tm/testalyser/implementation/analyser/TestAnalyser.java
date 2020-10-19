package com.tm.testalyser.implementation.analyser;

import com.tm.testalyser.Analyser;
import com.tm.testalyser.implementation.loader.TestLoader;
import com.tm.testalyser.model.ReportType;
import com.tm.testalyser.model.TestRun;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.tm.testalyser.model.TestStatus.FAIL;
import static com.tm.testalyser.model.TestStatus.PASS;
import static com.tm.testalyser.template.TestAnalyserReportGenerator.*;
import static java.util.stream.Collectors.toUnmodifiableList;

/**
 * Analyses all the test files in a given directory and produces a test test report.
 */
public class TestAnalyser implements Analyser<String> {

    private final Path directory;
    private final String testFileExtension;
    private final ReportType reportType;


    /**
     * @param directory         path of the directory containing the test files to be analysed;
     * @param testFileExtension extension of test files in the {@link #directory}, non-matching files are ignored;
     * @param reportType        {@link ReportType}
     */
    public TestAnalyser(final Path directory, final String testFileExtension, final ReportType reportType) {
        this.directory = directory;
        this.testFileExtension = testFileExtension;
        this.reportType = reportType;
    }

    /**
     * Output example for FLAKY_TESTS analysis type:
     * <p>
     * Testalyser analysis type: [FLAKY_TESTS].
     * <p>
     * Analysis completed for files: [graph_test.txt, lock_test_1.txt, lock_test_2.txt, rewrite_test.txt].
     * <p>
     * ---------------------------------------------
     * | TEST                      | LATEST_STATUS |
     * ---------------------------------------------
     * | TestAcquireRepoLock       | PASS          |
     * ---------------------------------------------
     *
     * @return a String format report with a structure determined by the analysis type, {@link ReportType}.
     */
    public String analyse() {
        List<TestRun> testRuns = new TestLoader(directory, testFileExtension).load();
        List<String> analysedTestFiles = testRuns.stream()
                .map(TestRun::getTestFileName)
                .distinct()
                .collect(toUnmodifiableList());

        switch (reportType) {
            case ALL_TESTS:
                return generateAllTestsReport(testRuns, analysedTestFiles);
            case ALL_TESTS_DURATION_SORTED:
                return generateAllTestsDurationSortedReport(testRuns.stream()
                        .sorted(Comparator.comparing(TestRun::getTestFileName).reversed())
                        .distinct()
                        .sorted(Comparator.comparing(TestRun::getDuration).reversed())
                        .collect(toUnmodifiableList()), analysedTestFiles);
            case PASSED_TESTS:
                return generatePassedTestsReport(testRuns.stream()
                        .filter(test -> test.getStatus().equals(PASS))
                        .distinct()
                        .collect(toUnmodifiableList()), analysedTestFiles);
            case FAILED_TESTS:
                return generateFailedTestsReport(testRuns.stream()
                        .filter(test -> test.getStatus().equals(FAIL))
                        .distinct()
                        .collect(toUnmodifiableList()), analysedTestFiles);
            case FLAKY_TESTS:
                List<TestRun> passingTestRuns =
                        testRuns.stream()
                                .filter(test -> test.getStatus().equals(PASS))
                                .collect(Collectors.toUnmodifiableList());
                List<TestRun> failingTestRuns =
                        testRuns.stream()
                                .filter(test -> test.getStatus().equals(FAIL))
                                .collect(Collectors.toUnmodifiableList());

                List<TestRun> flakyTestRuns = new ArrayList<>();

                BiConsumer<TestRun, TestRun> addTheLatestTestStatus =
                        (aTest, anotherTest) -> flakyTestRuns
                                .add(aTest.getTestFileName().compareTo(anotherTest.getTestFileName()) > 0
                                        ? aTest
                                        : anotherTest
                                );

                if (passingTestRuns.stream().anyMatch(failingTestRuns::contains)) {
                    passingTestRuns.stream().filter(failingTestRuns::contains)
                            .forEach(aTest -> failingTestRuns
                                    .forEach(anotherTest -> addTheLatestTestStatus.accept(aTest, anotherTest)));
                }

                return generateFlakyTestsReport(flakyTestRuns, analysedTestFiles);
        }

        return testRuns.toString();
    }
}
