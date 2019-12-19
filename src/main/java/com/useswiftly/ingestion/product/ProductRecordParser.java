package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.Field;
import com.useswiftly.ingestion.records.RecordParseException;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 */
public class ProductRecordParser implements Function<String, ProductRecord> {
    private final List<Field<?, ProductRecord>> fieldsToParse;
    private final int recordSize;
    private final Provider<ProductRecord> productRecordProvider;

    @Inject
    public ProductRecordParser(final List<Field<?, ProductRecord>> fieldsToParse,
                               final Provider<ProductRecord> productRecordProvider) {
        this.fieldsToParse = fieldsToParse;
        this.productRecordProvider = productRecordProvider;
        this.recordSize = calculateRecordSize();
    }

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
    public ProductRecord apply(final String line) {
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
