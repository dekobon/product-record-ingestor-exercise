package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.Field;
import com.useswiftly.ingestion.records.RecordParseException;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Closure that encapsulates the per-line record parsing logic for the product
 * record data file such that it can be used within a Java 8 stream.
 */
public class ProductRecordParser implements Function<String, ProductRecord> {
    /**
     * List of fields to be parsed from data file.
     */
    private final List<Field<?, ProductRecord>> fieldsToParse;

    /**
     * Total size in characters of a single line in the record data file.
     */
    private final int recordSize;

    /**
     * Provider class that provides new pre-injected instances of {@link ProductRecord}.
     */
    private final Provider<ProductRecord> productRecordProvider;

    @Inject
    public ProductRecordParser(final List<Field<?, ProductRecord>> fieldsToParse,
                               final Provider<ProductRecord> productRecordProvider) {
        this.fieldsToParse = fieldsToParse;
        this.productRecordProvider = productRecordProvider;
        this.recordSize = calculateRecordSize();
    }

    /**
     * Calculates the expected line length by finding the field with the boundary
     * furthest to the right.
     *
     * @return record size in characters
     */
    protected int calculateRecordSize() {
        final int recordSize;

        // Find the field position that is the furthest to the right
        try (Stream<? extends Field<?, ProductRecord>> stream = fieldsToParse.stream()) {
            recordSize = stream
                    .map(Field::getEndPositionExclusive)
                    .max(Integer::compare).orElseThrow();
        }

        return recordSize;
    }

    @Override
    public ProductRecord apply(@NotNull final String line) {
        if (line.length() != recordSize) {
            String msg = String.format("Expected a record length of [%d], " +
                    "actual length of record was [%d] characters. Line contents:\n%s",
                    recordSize, line.length(), line);
            throw new RecordParseException(msg);
        }

        final ProductRecord record = productRecordProvider.get();

        for (final Field<?, ProductRecord> field : fieldsToParse) {
            final String substring = line.substring(
                    field.getStartPositionInclusive(), field.getEndPositionExclusive());
            field.convertAndAssignValueToRecord(substring, record);
        }

        return record;
    }
}
