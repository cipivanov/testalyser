package com.tm.testalyser.implementation.loader;

import com.tm.testalyser.Loader;
import com.tm.testalyser.implementation.parser.TestParser;
import com.tm.testalyser.model.TestRun;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public class TestLoader implements Loader<TestRun> {

    private final Path testDirectory;
    private final String testFileExtension;

    public TestLoader(final Path testDirectory, final String testFileExtension) {
        this.testDirectory = testDirectory;
        this.testFileExtension = testFileExtension;
    }

    /**
     * @return a List of TestRun found in the {@link #testDirectory} contained in files with {@link #testFileExtension} extension.
     */
    @Override
    public List<TestRun> load() {
        try {
            return Files
                    .list(testDirectory)
                    .filter(path -> path.toString().endsWith(testFileExtension))
                    .flatMap(path -> new TestParser(path).parse().stream())
                    .collect(toUnmodifiableList());
        } catch (IOException ioe) {
            // swallow the exception, for simplicity, for now :)
        }

        return Collections.emptyList();
    }

    public Path getTestDirectory() {
        return testDirectory;
    }

    public String getTestFileExtension() {
        return testFileExtension;
    }
}
