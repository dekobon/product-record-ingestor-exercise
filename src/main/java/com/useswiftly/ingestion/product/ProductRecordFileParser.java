package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.RecordFileParser;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.stream.Stream;

/**
 * Provides an implementation of {@link RecordFileParser} that allows for
 * parsing fixed character width product record data files into a {@link Stream}
 * of {@link ProductRecord} objects.
 */
public class ProductRecordFileParser implements RecordFileParser<ProductRecord> {
    /**
     * Provider of ProductRecordParser object used so that we can have potentially
     * a different configuration of the parser per execution.
     */
    private final Provider<ProductRecordParser> productRecordParserProvider;

    @Inject
    public ProductRecordFileParser(final Provider<ProductRecordParser> productRecordParserProvider) {
        this.productRecordParserProvider = productRecordParserProvider;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Note: The caller of this method is responsible for closing the passed
     * reader resource.</p>
     *
     * @param reader source of data to parse
     * @return stream of records
     */
    @Override
    public Stream<ProductRecord> parse(@NotNull final Reader reader) {
        /* BufferedReader.close() only calls the inner reader that was wrapped
         * there is no need for us to explicitly close it. If this is a concern
         * in the future, we can use the .onClose() method on the stream being
         * returned to have the buffered reader instance closed when the stream
         * closes. */
        final BufferedReader bufferedReader;

        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader)reader;
        } else {
            bufferedReader = new BufferedReader(reader);
        }

        final ProductRecordParser recordParser = productRecordParserProvider.get();
        return bufferedReader.lines().map(recordParser);
    }
}
