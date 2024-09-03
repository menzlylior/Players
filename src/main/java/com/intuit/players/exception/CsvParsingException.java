package com.intuit.players.exception;

import java.util.List;

public class CsvParsingException extends RuntimeException {

    private final List<String> errorMessages;

    public CsvParsingException(List<String> errorMessages) {
        super("CSV Parsing encountered errors");
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
