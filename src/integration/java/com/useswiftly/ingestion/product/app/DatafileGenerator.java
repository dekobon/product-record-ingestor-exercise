package com.useswiftly.ingestion.product.app;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Test data file generator used to create large record files for performance
 * testing.
 */
public class DatafileGenerator {
    private static final String ZERO_VALUE = "00000000";
    private static final RandomStringGenerator STRING_GENERATOR =
            new RandomStringGenerator.Builder()
                    .withinRange('a', 'z')
                    .build();

    private static long productId = 0L;

    public static void writeLine(final Writer source) throws IOException {
        final boolean isSplitPrice = RandomUtils.nextBoolean();

        source.append(productId()).append(" ")
              .append(description()).append(" ")
              .append(isSplitPrice ? ZERO_VALUE : randomPrice()).append(" ")
              .append(isSplitPrice ? ZERO_VALUE : randomPrice()).append(" ")
              .append(isSplitPrice ? randomPrice() : ZERO_VALUE).append(" ")
              .append(isSplitPrice ? randomPrice() : ZERO_VALUE).append(" ")
              .append(isSplitPrice ? randomForX() : ZERO_VALUE).append(" ")
              .append(isSplitPrice ? randomForX() : ZERO_VALUE).append(" ")
              .append(flags()).append(" ")
              .append(productSize());
    }

    private static String productId() {
        ++productId;
        return String.format("%08d", productId);
    }

    private static String description() {
        final String randomText = STRING_GENERATOR.generate(1, 59);
        return StringUtils.rightPad(randomText, 59);
    }

    private static String randomPrice() {
        int number = RandomUtils.nextInt(0, 100000000);

        if (number < 10000 && RandomUtils.nextBoolean()) {
            number = number * -1;
        }

        return String.format("%08d", number);
    }

    private static String randomForX() {
        final int number = RandomUtils.nextInt(0, 1000);
        return String.format("%08d", number);
    }

    private static String flags() {
        final char[] flags = new char[9];

        for (int i = 0; i < flags.length; i++) {
            flags[i] = RandomUtils.nextBoolean() ? 'Y' : 'N';
        }

        return String.valueOf(flags);
    }

    private static String productSize() {
        final String randomText = STRING_GENERATOR.generate(0, 9);
        return StringUtils.leftPad(randomText, 9);
    }

    public static void writeTestData(final Writer writer, final long lines) throws IOException {
        for (int i = 0; i < lines; i++) {
            writeLine(writer);
            writer.append(System.lineSeparator());
        }
    }

    public static void main(final String[] argv) throws IOException{
        final long lines = Long.parseLong(argv[0]);
        final File file = new File("/tmp/large-sample.txt");

        try (final Writer writer = new FileWriter(file);
             final BufferedWriter bw = new BufferedWriter(writer)) {
            writeTestData(bw, lines);
        }
    }
}
