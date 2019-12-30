package com.useswiftly.ingestion.product.app;

import com.useswiftly.ingestion.product.ProductRecord;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * JMH benchmarking class used to assess the performance of parsing product
 * records. The key interesting comparison here is the difference in performance
 * between single threaded stream parsing and multi-threaded stream parsing.
 */
@State(Scope.Benchmark)
public class ProductRecordParseBenchmark {
    private Path recordsFile;
    private Stream<ProductRecord> stream;

    @Setup(Level.Invocation)
    public void setup() throws IOException {
        try {
            this.recordsFile = Paths.get(ClassLoader.getSystemResource(
                    "large-sample.txt").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unable to read test data file. " +
                    "Did you use git-lfs to download it to the repository?", e);
        }

        this.stream = Application.parsePathForRecordsData(recordsFile);
    }

    @TearDown(Level.Invocation)
    public void tearDown() {
        this.stream.close();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public long singleThreaded(final Blackhole blackhole) {
        return stream
                .peek(blackhole::consume)
                .count();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public long parallelStream(final Blackhole blackhole) {
        return stream
                .parallel()
                .peek(blackhole::consume)
                .count();
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
