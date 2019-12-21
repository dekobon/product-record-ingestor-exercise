package com.useswiftly.ingestion.product.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.product.ProductRecordFileParser;

import java.io.File;
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
    /**
     * Entry point into the application
     * @param argv String array with the first element containing the path to the file to parse
     */
    public static void main(final String[] argv) {
        if (argv.length == 0) {
            System.err.println("Missing path to the data file as the" +
                    "first program argument");
            System.exit(1);
        }

        final Path path = openFileAtPath(argv[0]);
        final Injector injector = Guice.createInjector(new ProductRecordIngestorModule());
        final ProductRecordFileParser parser =
                injector.getInstance(ProductRecordFileParser.class);
        final Charset charset = injector.getInstance(Charset.class);

        try (final Reader source = Files.newBufferedReader(path, charset);
             final Stream<ProductRecord> records = parser.parse(source)) {
            records.forEach(System.out::println);

            /* If we needed to turn this stream into a collection as written in
             * the specification, we could do the following:
             * records.collect(Collectors.toList()); */
        } catch (Exception e) {
            System.err.printf("Error processing data file: %s\n", path);
            e.printStackTrace(System.err);
            System.exit(1);
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
