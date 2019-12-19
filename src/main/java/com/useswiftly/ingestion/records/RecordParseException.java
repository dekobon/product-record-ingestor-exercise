package com.useswiftly.ingestion.records;

/**
 *
 */
public class RecordParseException extends RuntimeException {
    public RecordParseException(final String message) {
        super(message);
    }

    public RecordParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
