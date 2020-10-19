package com.tm.testalyser.template;

import com.tm.testalyser.model.ReportType;
import com.tm.testalyser.model.TestRun;

import java.util.List;
import java.util.function.Function;

/**
 * Utility class capable of jigsawing together the report based on the type requested. My great shame.
 */
public class TestAnalyserReportGenerator {

    // in a library-allowed world the constructs below are replaced by a template file, interpreted by a template engine
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static final String ANALYSIS_TYPE = "Testalyser analysis type: [%s].";
    private static final String ANALYSIS_FILES = "Analysis completed for files: [%s].";

    private static final String TEST_LIST_SEPARATOR =
            String.format("%-50s", "").replace(" ", "-");
    private static final String TEST_LIST_HEADER =
            String.format("| %-25s | %-6s | %-9s |" + LINE_SEPARATOR, "TEST", "STATUS", "DURATION");
    private static final Function<TestRun, String> TEST_LIST_BODY =
            (test) -> String.format("| %-25s | %-6s | %-9s |" + LINE_SEPARATOR, test.getName(), test.getStatus(), test.getDuration());

    private static final String FLAKY_TEST_LIST_SEPARATOR =
            String.format("%-45s", "").replace(" ", "-");
    private static final String FLAKY_TEST_LIST_HEADER =
            String.format("| %-25s | %-13s |" + LINE_SEPARATOR, "TEST", "LATEST_STATUS");
    private static final Function<TestRun, String> FLAKY_TEST_LIST_BODY =
            (test) -> String.format("| %-25s | %-13s |" + LINE_SEPARATOR, test.getName(), test.getStatus(), test.getDuration());

    public static String generateAllTestsReport(List<TestRun> testRuns, List<String> analysedFiles) {
        return generateTestReportWithMode(testRuns, ReportType.ALL_TESTS, TEST_LIST_HEADER, TEST_LIST_SEPARATOR, TEST_LIST_BODY, analysedFiles);
    }

    public static String generatePassedTestsReport(List<TestRun> testRuns, List<String> analysedFiles) {
        return generateTestReportWithMode(testRuns, ReportType.PASSED_TESTS, TEST_LIST_HEADER, TEST_LIST_SEPARATOR, TEST_LIST_BODY, analysedFiles);
    }

    public static String generateFailedTestsReport(List<TestRun> testRuns, List<String> analysedFiles) {
        return generateTestReportWithMode(testRuns, ReportType.FAILED_TESTS, TEST_LIST_HEADER, TEST_LIST_SEPARATOR, TEST_LIST_BODY, analysedFiles);
    }

    public static String generateAllTestsDurationSortedReport(List<TestRun> testRuns, List<String> analysedFiles) {
        return generateTestReportWithMode(testRuns, ReportType.ALL_TESTS_DURATION_SORTED, TEST_LIST_HEADER, TEST_LIST_SEPARATOR, TEST_LIST_BODY, analysedFiles);
    }

    public static String generateFlakyTestsReport(List<TestRun> testRuns, List<String> analysedFiles) {
        return generateTestReportWithMode(testRuns, ReportType.FLAKY_TESTS, FLAKY_TEST_LIST_HEADER, FLAKY_TEST_LIST_SEPARATOR, FLAKY_TEST_LIST_BODY, analysedFiles);
    }

    private static String generateTestReportWithMode(List<TestRun> testRuns, ReportType type, String header, String separator, Function<TestRun, String> body, List<String> analysedFiles) {
        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append(LINE_SEPARATOR)
                .append(String.format(ANALYSIS_TYPE, type))
                .append(LINE_SEPARATOR)
                .append(LINE_SEPARATOR)
                .append(String.format(ANALYSIS_FILES, String.join(", ", analysedFiles)))
                .append(LINE_SEPARATOR)
                .append(LINE_SEPARATOR)
                .append(separator)
                .append(LINE_SEPARATOR)
                .append(header)
                .append(separator)
                .append(LINE_SEPARATOR);

        testRuns.forEach(test -> sBuilder.append(body.apply(test)));

        sBuilder.append(separator);

        return sBuilder.toString();
    }
}
