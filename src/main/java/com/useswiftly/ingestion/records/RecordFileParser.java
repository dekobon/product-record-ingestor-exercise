package com.useswiftly.ingestion.records;

import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.util.stream.Stream;

/**
 * Interface representing record data file parsing functionality.
 */
public interface RecordFileParser<RECORD_TYPE extends Record> {
    /**
     * Parses a given data source as a stream of records.
     *
     * @param reader source of data to parse
     * @return stream of records
     */
    Stream<RECORD_TYPE> parse(@NotNull final Reader reader);
}
