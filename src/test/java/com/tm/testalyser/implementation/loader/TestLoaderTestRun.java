package com.tm.testalyser.implementation.loader;

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

class TestLoaderTestRun {

    private final Path testResultsLocation = Path.of("build");
    private final String testResultFileOneName = "test_file_1.txt";
    private final String testResultFileTwoName = "test_file_2.txt";

    @BeforeEach
    public void setup() throws IOException {
        Path fileOne = Files.createFile(testResultsLocation.resolve(testResultFileOneName));
        Path fileTwo = Files.createFile(testResultsLocation.resolve(testResultFileTwoName));

        String fileOneContent =
                "--> START //src/core:testRun\n" +
                        "=== RUN   TestOne\n" +
                        "--- FAIL: TestOne (1.01s)\n" +
                        "=== RUN   TestTwo\n" +
                        "--- PASS: TestTwo (1.02s)\n" +
                        "--- SKIP: TestThree (1.03s)\n" +
                        "FAIL\n";

        String fileTwoContent =
                "--> START //src/core:testRun\n" +
                        "=== RUN   TestOne\n" +
                        "--- PASS: TestOne (1.04s)\n" +
                        "PASS\n";

        Files.write(fileOne, fileOneContent.getBytes());
        Files.write(fileTwo, fileTwoContent.getBytes());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(testResultsLocation.resolve(testResultFileOneName));
        Files.deleteIfExists(testResultsLocation.resolve(testResultFileTwoName));
    }

    // this test indirectly will test the parser, not truly "unit"
    @Test
    public void shouldLoadListOfTestFromTestResultFile() {
        List<TestRun> expectedTestRuns = Arrays.asList(
                new TestRun("TestOne", TestStatus.FAIL, "1.01s", testResultFileOneName),
                new TestRun("TestTwo", TestStatus.PASS, "1.02s", testResultFileOneName),
                new TestRun("TestThree", TestStatus.SKIP, "1.03s", testResultFileOneName),
                new TestRun("TestOne", TestStatus.PASS, "1.04s", testResultFileTwoName)
        );
        List<TestRun> actualTestRuns =
                new TestLoader(testResultsLocation, ".txt").load();

        // will not assert correctly on duplicates, consider using a Harmcrest-like matchers
        assertTrue(expectedTestRuns.size() == actualTestRuns.size() &&
                expectedTestRuns.containsAll(actualTestRuns) && actualTestRuns.containsAll(expectedTestRuns));
    }
}