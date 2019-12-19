package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.Field;
import com.useswiftly.ingestion.records.RecordFileParser;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 */
public class ProductRecordFileParser implements RecordFileParser<ProductRecord> {
    private final List<Field<?, ProductRecord>> fieldsToParse;
    private final Provider<ProductRecord> productRecordProvider;

    @Inject
    public ProductRecordFileParser(final Provider<ProductRecord> productRecordProvider,
                                   final List<Field<?, ProductRecord>> fieldsToParse) {
        this.productRecordProvider = productRecordProvider;
        this.fieldsToParse = fieldsToParse;
    }

    @Override
    public Stream<ProductRecord> parse(@NotNull final Reader reader) {
        final BufferedReader bufferedReader;

        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader)reader;
        } else {
            bufferedReader = new BufferedReader(reader);
        }

        ProductRecordParser recordParser = new ProductRecordParser(fieldsToParse, productRecordProvider);
        return bufferedReader.lines().map(recordParser);
    }
}
