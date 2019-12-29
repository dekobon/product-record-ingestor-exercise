package com.useswiftly.ingestion.product.app;

import com.useswiftly.ingestion.product.ProductRecord;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.util.NullOutputStream;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * JMH benchmarking class used to assess the performance of parsing product
 * records. The key interesting comparison here is the difference in performance
 * between single threaded stream parsing and multi-threaded stream parsing.
 */
@State(Scope.Benchmark)
public class ProductRecordParseBenchmark {
    private Path recordsFile;

    @Setup
    public void setup() {
        try {
            this.recordsFile = Paths.get(ClassLoader.getSystemResource(
                    "large-sample.txt").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unable to read test data file. " +
                    "Did you use git-lfs to download it to the repository?", e);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public OutputStream singleThreaded() throws IOException {
        try (OutputStream noopOut = new NullOutputStream();
             Stream<ProductRecord> stream = Application.parsePathForRecordsData(recordsFile)) {
            stream.forEach(r -> {
                try {
                    noopOut.write(r.toString().getBytes(US_ASCII));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            return noopOut;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public OutputStream parallelStream() throws IOException {
        try (OutputStream noopOut = new NullOutputStream();
             Stream<ProductRecord> stream = Application.parsePathForRecordsData(recordsFile)) {
            stream.parallel().forEach(r -> {
                try {
                    noopOut.write(r.toString().getBytes(US_ASCII));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            return noopOut;
        }
    }

    private static void runBenchmarks() throws RunnerException {
        final Options opt = new OptionsBuilder()
                .include(ProductRecordParseBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Test
    public void benchmarks() throws RunnerException {
        runBenchmarks();
    }

    public static void main(String[] args) throws RunnerException {
        runBenchmarks();
    }
}
