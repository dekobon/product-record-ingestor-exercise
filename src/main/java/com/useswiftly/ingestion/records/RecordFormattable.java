package com.useswiftly.ingestion.records;

/**
 * Interface representing the functionality for formatting a {@link Record}
 * implementation to an arbitrary {@link String}.
 */
public interface RecordFormattable<RECORD_TYPE extends Record> {
    /**
     * Method allowing a {@link Record} implementation to be formatted in an
     * extensible manner.
     *
     * @param record record to format
     * @return String representation of the record
     */
    String format(RECORD_TYPE record);
}
