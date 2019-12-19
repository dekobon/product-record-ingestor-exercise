package com.useswiftly.ingestion.product.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.product.ProductRecordFileParser;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Class providing a static main method to allow the record ingestion
 * application to run as a stand-alone program.
 */
public class Application {
    /**
     * Entry point into the application
     * @param argv String array with the first element containing the path to the file to parse
     * @throws IOException thrown when there is a problem parsing the supplied file
     */
    public static void main(final String[] argv) throws IOException {
        final Injector injector = Guice.createInjector(new ProductRecordIngestorModule());
        final ProductRecordFileParser parser =
                injector.getInstance(ProductRecordFileParser.class);
        final Charset charset = injector.getInstance(Charset.class);

        final File file = new File(argv[0]);
        final Reader source = Files.newBufferedReader(file.toPath(), charset);

        try (Stream<ProductRecord> records = parser.parse(source)) {
            records.forEach(System.out::println);
        }
    }
}
