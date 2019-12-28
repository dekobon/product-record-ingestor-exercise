package com.useswiftly.ingestion.product.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.product.ProductRecordFileParser;
import com.useswiftly.ingestion.records.RecordFormattable;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Class providing a static main method to allow the record ingestion
 * application to run as a stand-alone program.
 */
public class Application {
    static final Injector injector = Guice.createInjector(new ProductRecordIngestorModule());

    /**
     * Entry point into the application
     * @param argv String array with the first element containing the path to the file to parse
     */
    public static void main(final String[] argv) {
        if (argv.length == 0) {
            System.err.println("com.useswiftly.ingestion.product.app.Application: " +
                    "The first argument must be the path to the product record data file");
            System.exit(1);
        }

        final Path path = openFileAtPath(argv[0]);

        /* Interestingly, generic type inference will not work below with the
         * Java 11 compiler. */
        //noinspection Convert2Diamond
        final RecordFormattable<ProductRecord> formatter = injector.getInstance(
                Key.get(new TypeLiteral<RecordFormattable<ProductRecord>>() {}));

        try (final Stream<ProductRecord> records = parsePathForRecordsData(path)) {
            records.map(formatter::format)
                   .forEach(System.out::println);

            /* If we needed to turn this stream into a collection as written in
             * the specification, we could do the following:
             * records.collect(Collectors.toList()); */
        } catch (Exception e) {
            System.err.printf("Error processing data file: %s\n", path);
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    static Stream<ProductRecord> parsePathForRecordsData(final Path path)
            throws IOException {
        final ProductRecordFileParser parser =
                injector.getInstance(ProductRecordFileParser.class);
        final Charset charset = injector.getInstance(Charset.class);

        final Reader source = Files.newBufferedReader(path, charset);
        final Stream<ProductRecord> records = parser.parse(source);

        return records.onClose(() -> {
            try {
                source.close();
            } catch (IOException e) {
                System.err.printf("Unable to close records at path: %s\n",
                        path);
                e.printStackTrace(System.err);
            }
        });
    }

    private static Path openFileAtPath(final String filePath) {
        final File file = new File(filePath);

        if (!file.exists()) {
            System.err.printf("Data file not found at path: %s\n", filePath);
            System.exit(1);
        }

        if (!file.canRead()) {
            System.err.printf("Data file could not be read at path: %s\n", filePath);
            System.exit(1);
        }

        if (!file.isFile()) {
            System.err.printf("Path does not specify a valid file: %s\n", filePath);
            System.exit(1);
        }

        return file.toPath();
    }
}
