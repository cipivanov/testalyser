package com.tm.testalyser.cli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CommandLineInterfaceTestRun {

    @Test
    public void shouldFailWithValidationOnTooFewArguments() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> CommandLineInterface.main(new String[]{"First", "Second"}));

        assertEquals("Testalyser expects three arguments!", exception.getMessage());
    }

    @Test
    public void shouldFailWithValidationOnTooManyArguments() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> CommandLineInterface.main(new String[]{"First", "Second", "Third", "Fourth"}));

        assertEquals("Testalyser expects three arguments!", exception.getMessage());
    }

    @Test
    public void shouldFailWithValidationOnNonExistingLocation() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> CommandLineInterface.main(new String[]{"/some/location/i/never/heard/before", "Second", "Third"}));

        assertEquals("The directory path provided does not exist, path: /some/location/i/never/heard/before", exception.getMessage());
    }
}