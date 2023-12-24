package com.springboot.code.example.database.benchmark;

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
import com.springboot.code.example.database.domain.name.NamePrefixRepository;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class NameInfoBenmark extends BenchmarkConfig {

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

  @Override
  String setProfile() {

    return "benchmark";
  }

  @Autowired
  NamePrefixRepository namePrefixRepository;

  private static final String START_DATE = "2023-12-22";
  private static final String TO_DATE = "2023-12-24";

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