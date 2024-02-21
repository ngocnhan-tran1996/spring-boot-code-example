package io.ngocnhan_tran1996.code.example.database.benchmark;

import io.ngocnhan_tran1996.code.example.database.domain.NamePrefixRepository;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class NameInfoBenmark extends BenchmarkConfig {

    private static final String START_DATE = "2023-12-22";
    private static final String TO_DATE = "2023-12-24";
    @Autowired
    NamePrefixRepository namePrefixRepository;

    public static void main(String[] args) throws RunnerException {

        var opt = new OptionsBuilder()
            .include(NameInfoBenmark.class.getSimpleName())
            .warmupIterations(1)
            .measurementIterations(5)
            .forks(1)
            .warmupForks(1)
            .threads(1)
            .build();

        new Runner(opt).run();
    }

    @Benchmark
    public Page<Object[]> executeNameInfoFirstPage() {

        return namePrefixRepository.executeNameInfo(
            START_DATE,
            TO_DATE,
            PageRequest.of(0, 100));
    }

    @Benchmark
    public Page<Object[]> executeNameInfo() {

        return namePrefixRepository.executeNameInfo(
            START_DATE,
            TO_DATE,
            PageRequest.of(499, 100));
    }

    @Benchmark
    public Page<Object[]> executeNameInfoLoopFirstPage() {

        return namePrefixRepository.executeNameInfoLoop(
            START_DATE,
            TO_DATE,
            PageRequest.of(0, 100));
    }

    @Benchmark
    public Page<Object[]> executeNameInfoLoop() {

        return namePrefixRepository.executeNameInfoLoop(
            START_DATE,
            TO_DATE,
            PageRequest.of(499, 100));
    }

    @Benchmark
    public List<Object[]> paginateNameInfoFirstPage() {

        return namePrefixRepository.paginateNameInfo(
            START_DATE,
            TO_DATE,
            0,
            100);
    }

    @Benchmark
    public List<Object[]> paginateNameInfo() {

        return namePrefixRepository.paginateNameInfo(
            START_DATE,
            TO_DATE,
            49_900,
            50_000);
    }

    @Benchmark
    public List<Object[]> paginateNameInfoLoopFirstPage() {

        return namePrefixRepository.paginateNameInfoLoop(
            START_DATE,
            TO_DATE,
            0,
            100);
    }

    @Benchmark
    public List<Object[]> paginateNameInfoLoop() {

        return namePrefixRepository.paginateNameInfoLoop(
            START_DATE,
            TO_DATE,
            49_900,
            50_000);
    }

}