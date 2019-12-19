package com.useswiftly.ingestion.records;

import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.util.stream.Stream;

/**
 *
 */
public interface RecordFileParser<T extends Record> {
    Stream<T> parse(@NotNull final Reader reader);
}
