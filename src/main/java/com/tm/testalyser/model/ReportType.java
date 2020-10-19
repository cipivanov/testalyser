package com.tm.testalyser.model;

/**
 * Hosts all the report types available to the TestAnalyser.
 *
 * ALL_TESTS - outputs a report of all the tests parsed, nothing special;
 * FLAKY_TESTS - outputs a report of flaky tests, tests that have registered a failure in the past and their current status;
 * PASSED_TESTS - outputs a report of passed tests, duration included;
 * FAILED_TESTS - outputs a report of failed tests, duration included;
 * ALL_TESTS_DURATION_SORTED - outputs a report of all tests, sorted in descending order based on duration;
 */
public enum ReportType {
    ALL_TESTS,
    FLAKY_TESTS,
    PASSED_TESTS,
    FAILED_TESTS,
    ALL_TESTS_DURATION_SORTED
}
