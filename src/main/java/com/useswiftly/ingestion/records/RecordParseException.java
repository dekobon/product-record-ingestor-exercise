package com.useswiftly.ingestion.records;

/**
 *
 */
public class RecordParseException extends RuntimeException {
    private static final long serialVersionUID = 4280921967560275961L;

    public RecordParseException(final String message) {
        super(message);
    }

    public RecordParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
