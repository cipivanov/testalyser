package com.tm.testalyser.implementation.analyser;

import com.tm.testalyser.model.ReportType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestAnalyserTest {

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

    // this test indirectly will test the parser and analyser, not truly "unit"
    @Test
    public void shouldAnalyseWithAllTestsType() {
        String expectedReport =
                "\nTestalyser analysis type: [ALL_TESTS].\n" +
                        "\n" +
                        "Analysis completed for files: [test_file_1.txt, test_file_2.txt].\n" +
                        "\n" +
                        "--------------------------------------------------\n" +
                        "| TEST                      | STATUS | DURATION  |\n" +
                        "--------------------------------------------------\n" +
                        "| TestTwo                   | PASS   | 1.02s     |\n" +
                        "| TestOne                   | FAIL   | 1.01s     |\n" +
                        "| TestThree                 | SKIP   | 1.03s     |\n" +
                        "| TestOne                   | PASS   | 1.04s     |\n" +
                        "--------------------------------------------------";
        String actualReport = new TestAnalyser(testResultsLocation, ".txt", ReportType.ALL_TESTS).analyse();

        assertEquals(expectedReport, actualReport);
    }

    @Test
    public void shouldAnalyseWithFlakyTestsType() {
        String expectedReport =
                "\nTestalyser analysis type: [FLAKY_TESTS].\n" +
                        "\n" +
                        "Analysis completed for files: [test_file_1.txt, test_file_2.txt].\n" +
                        "\n" +
                        "---------------------------------------------\n" +
                        "| TEST                      | LATEST_STATUS |\n" +
                        "---------------------------------------------\n" +
                        "| TestOne                   | PASS          |\n" +
                        "---------------------------------------------";
        String actualReport = new TestAnalyser(testResultsLocation, ".txt", ReportType.FLAKY_TESTS).analyse();

        assertEquals(expectedReport, actualReport);
    }

    @Test
    public void shouldAnalyseWithPassedTestsType() {
        String expectedReport =
                "\nTestalyser analysis type: [PASSED_TESTS].\n" +
                        "\n" +
                        "Analysis completed for files: [test_file_1.txt, test_file_2.txt].\n" +
                        "\n" +
                        "--------------------------------------------------\n" +
                        "| TEST                      | STATUS | DURATION  |\n" +
                        "--------------------------------------------------\n" +
                        "| TestTwo                   | PASS   | 1.02s     |\n" +
                        "| TestOne                   | PASS   | 1.04s     |\n" +
                        "--------------------------------------------------";
        String actualReport = new TestAnalyser(testResultsLocation, ".txt", ReportType.PASSED_TESTS).analyse();

        assertEquals(expectedReport, actualReport);
    }

    @Test
    public void shouldAnalyseWithAllTestsPassedDurationSortedType() {
        String expectedReport =
                "\nTestalyser analysis type: [ALL_TESTS_DURATION_SORTED].\n" +
                        "\n" +
                        "Analysis completed for files: [test_file_1.txt, test_file_2.txt].\n" +
                        "\n" +
                        "--------------------------------------------------\n" +
                        "| TEST                      | STATUS | DURATION  |\n" +
                        "--------------------------------------------------\n" +
                        "| TestOne                   | PASS   | 1.04s     |\n" +
                        "| TestThree                 | SKIP   | 1.03s     |\n" +
                        "| TestTwo                   | PASS   | 1.02s     |\n" +
                        "--------------------------------------------------";
        String actualReport = new TestAnalyser(testResultsLocation, ".txt", ReportType.ALL_TESTS_DURATION_SORTED).analyse();

        assertEquals(expectedReport, actualReport);
    }
}
