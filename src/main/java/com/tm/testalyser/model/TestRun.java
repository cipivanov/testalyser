package com.tm.testalyser.model;

import java.util.Objects;

public class TestRun {

    private String name;
    private TestStatus status;
    private String output;
    private String duration;
    private String testResultFileName;

    public TestRun(String name, TestStatus status, String duration, String testResultFileName) {
        this.name = name;
        this.status = status;
        this.duration = duration;
        this.testResultFileName = testResultFileName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public String getTestFileName() {
        return testResultFileName;
    }

    public void setTestResultFileName(String testResultFileName) {
        this.testResultFileName = testResultFileName;
    }

    public String getDuration() {
        return Objects.requireNonNullElse(duration, "N/A");
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRun testRun = (TestRun) o;
        return name.equals(testRun.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Test{" +
                "name='" + name + '\'' +
                ", status=" + status +
                ", output='" + output + '\'' +
                ", duration='" + duration + '\'' +
                ", testResultFileName='" + testResultFileName + '\'' +
                '}';
    }
}
