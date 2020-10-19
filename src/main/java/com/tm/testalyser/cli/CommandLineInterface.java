package com.tm.testalyser.cli;

import com.tm.testalyser.implementation.analyser.TestAnalyser;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.tm.testalyser.model.ReportType.valueOf;

public class CommandLineInterface {

    public static void main(String[] args) {
        validateArguments(args);
        System.out.println(new TestAnalyser(Path.of(args[0]), args[1], valueOf(args[2])).analyse());
    }

    private static void validateArguments(final String[] arguments) {
        if (arguments.length != 3) {
            throw new IllegalArgumentException("Testalyser expects three arguments!");
        }

        if (!Files.exists(Path.of(arguments[0]))) {
            throw new IllegalArgumentException(String.format("The directory path provided does not exist, path: %s", Path.of(arguments[0])));
        }
    }
}
