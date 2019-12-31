package com.useswiftly.ingestion.product.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.product.ProductRecordFileParser;
import com.useswiftly.ingestion.records.RecordFormattable;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

/**
 * Class providing a static main method to allow the record ingestion
 * application to run as a stand-alone program.
 */
public class Application {
    static final Injector injector = Guice.createInjector(new ProductRecordIngestorModule());

    /**
     * Entry point into the application
     * @param argv String array with the first element containing the path or URL to the records data
     */
    public static void main(final String[] argv) {
        if (argv.length == 0) {
            System.err.println("com.useswiftly.ingestion.product.app.Application: " +
                    "The first argument must be the path or URL to a valid " +
                    "product record data file");
            System.exit(1);
        }

        /* Interestingly, generic type inference will not work below with the
         * Java 11 compiler. */
        //noinspection Convert2Diamond
        final RecordFormattable<ProductRecord> formatter = injector.getInstance(
                Key.get(new TypeLiteral<RecordFormattable<ProductRecord>>() {}));

        try (final Stream<ProductRecord> records = parseForRecordsData(argv[0])) {
            records.map(formatter::format)
                   .forEach(System.out::println);

            /* If we needed to turn this stream into a collection as written in
             * the specification, we could do the following:
             * records.collect(Collectors.toList()); */
        } catch (Exception e) {
            System.err.printf("Error processing data file: %s\n", argv[0]);
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    /**
     * Loads the product record stream from the passed NIO path location.
     *
     * <p><strong>Note:</strong> Be sure to close the stream when finished
     * because there are resources associated with the stream that need to be
     * closed.</p>
     *
     * @param path NIO path location in which to load product record data
     * @return stream of product records
     */
    static Stream<ProductRecord> parsePathForRecordsData(final Path path)
            throws IOException {
        // All linked resources are closed when the stream is closed
        final InputStream in = Files.newInputStream(path, StandardOpenOption.READ);
        return parseInputStreamForRecordsData(in);
    }

    /**
     * Loads the product record stream from the passed binary input stream.
     *
     * <p><strong>Note:</strong> The passed {@link InputStream} will be closed
     * when the returned {@link Stream} is closed.
     *
     * @param in input stream containing product record data
     * @return stream of product records
     */
    static Stream<ProductRecord> parseInputStreamForRecordsData(final InputStream in) {
        final ProductRecordFileParser parser =
                injector.getInstance(ProductRecordFileParser.class);
        final Charset charset = injector.getInstance(Charset.class);

        // All linked resources are closed when the stream is closed
        final Reader source = new InputStreamReader(in, charset);
        final BufferedReader bufferedReader = new BufferedReader(source);
        final Stream<ProductRecord> records = parser.parse(bufferedReader);

        return records.onClose(() -> {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                System.err.println("Unable to close records stream");
                e.printStackTrace(System.err);
            }
        });
    }

    /**
     * Attempts to determine if the passed string is a URL or a local file path
     * and loads the product record stream from the appropriate data source.
     *
     * @param dataLocation URL or local file path to a records data file
     * @return stream of product records
     * @throws IOException thrown if there is a problem loading the records data file
     */
    static Stream<ProductRecord> parseForRecordsData(@NotNull final String dataLocation)
            throws IOException {
        try {
            final URL url = new URL(dataLocation);
            return parseInputStreamForRecordsData(url.openStream());
        } catch (MalformedURLException | IllegalArgumentException e) {
            final Path path = openFileAtPath(dataLocation);
            return parsePathForRecordsData(path);
        }
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
