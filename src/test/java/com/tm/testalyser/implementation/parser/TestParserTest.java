package com.tm.testalyser.implementation.parser;

import com.tm.testalyser.model.TestStatus;
import com.tm.testalyser.model.TestRun;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TestParserTest {

    private final Path testResultsLocation = Path.of("build");
    private final String testResultFile = "test_file_1.txt";

    @BeforeEach
    public void setup() throws IOException {
        Path testFile = Files.createFile(testResultsLocation.resolve(testResultFile));

        String fileContent =
                "--> START //src/core:testRun\n" +
                        "=== RUN   TestOne\n" +
                        "--- FAIL: TestOne (1.01s)\n" +
                        "=== RUN   TestTwo\n" +
                        "--- PASS: TestTwo (1.02s)\n" +
                        "--- SKIP: TestThree (1.03s)\n" +
                        "FAIL\n";
        Files.write(testFile, fileContent.getBytes());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(testResultsLocation.resolve(testResultFile));
    }

    @Test
    public void shouldParseTestFileTests() {
        List<TestRun> expectedTestRuns = Arrays.asList(
                new TestRun("TestOne", TestStatus.FAIL, "1.01s", testResultFile),
                new TestRun("TestTwo", TestStatus.PASS, "1.02s", testResultFile),
                new TestRun("TestThree", TestStatus.SKIP, "1.03s", testResultFile)
        );
        List<TestRun> actualTestRuns = new TestParser(testResultsLocation.resolve(testResultFile)).parse();

        // will not assert correctly on duplicates, consider using a Harmcrest-like matchers
        assertTrue(expectedTestRuns.size() == actualTestRuns.size() &&
                expectedTestRuns.containsAll(actualTestRuns) && actualTestRuns.containsAll(expectedTestRuns));
    }
}