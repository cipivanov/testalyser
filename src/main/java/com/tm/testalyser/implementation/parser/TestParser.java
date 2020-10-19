package com.tm.testalyser.implementation.parser;

import com.tm.testalyser.Parser;
import com.tm.testalyser.model.TestRun;
import com.tm.testalyser.model.TestStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestParser implements Parser<TestRun> {

    private final Path testFile;

    public TestParser(Path testFile) {
        this.testFile = testFile;
    }

    /**
     * Currently tightly bound to the testing go execution output format/structure.
     *
     * @return a List of TestRun instances found in a specific {@link #testFile}.
     */
    @Override
    public List<TestRun> parse() {
        List<TestRun> testRuns = new ArrayList<>();
        String testFileName = testFile.getFileName().toString();
        String testFileContents;

        try {
            testFileContents = Files.readString(testFile);
        } catch (IOException e) {
            testFileContents = ""; // TODO: this stinks
        }

        testRuns.addAll(parsePassedTests(testFileName, testFileContents));
        testRuns.addAll(parseFailedTests(testFileName, testFileContents));
        testRuns.addAll(parseSkippedTests(testFileName, testFileContents));

        return testRuns;
    }

    private List<TestRun> parsePassedTests(String testFileName, String testFileContents) {
        List<TestRun> passedTestRuns = new ArrayList<>();
        Matcher matcher = Pattern.compile("--- PASS:\\s*(.*)\\s\\((.*)\\)").matcher(testFileContents);

        while (matcher.find()) {
            passedTestRuns.add(new TestRun(matcher.group(1), TestStatus.PASS, matcher.group(2), testFileName));
        }

        return passedTestRuns;
    }

    private List<TestRun> parseFailedTests(String testFileName, String testFileContents) {
        List<TestRun> failedTestRuns = new ArrayList<>();
        Matcher matcher = Pattern.compile("--- FAIL:\\s*(.*)\\s\\((.*)\\)").matcher(testFileContents);

        while (matcher.find()) {
            failedTestRuns.add(new TestRun(matcher.group(1), TestStatus.FAIL, matcher.group(2), testFileName));
        }

        return failedTestRuns;
    }

    private List<TestRun> parseSkippedTests(String testFileName, String testFileContents) {
        List<TestRun> skippedTestRuns = new ArrayList<>();
        Matcher matcher = Pattern.compile("--- SKIP:\\s*(.*)\\s\\((.*)\\)").matcher(testFileContents);

        while (matcher.find()) {
            skippedTestRuns.add(new TestRun(matcher.group(1), TestStatus.SKIP, matcher.group(2), testFileName));
        }

        return skippedTestRuns;
    }
}
