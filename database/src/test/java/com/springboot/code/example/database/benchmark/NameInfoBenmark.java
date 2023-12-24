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
import com.springboot.code.example.database.domain.name.NamePrefixProjection;
import com.springboot.code.example.database.domain.name.NamePrefixRepository;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class NameInfoBenmark extends BenchmarkConfig {

  public static void main(String[] args) throws RunnerException {

    var opt = new OptionsBuilder()
        .include(NameInfoBenmark.class.getSimpleName())
        .warmupIterations(1)
        .measurementIterations(3)
        .forks(1)
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

  @Benchmark
  public Page<NamePrefixProjection> executeNameInfoFirstPage() {

    return namePrefixRepository.executeNameInfo(
        "2023-12-18",
        "2023-12-20",
        PageRequest.of(0, 100));
  }

  @Benchmark
  public Page<NamePrefixProjection> executeNameInfo() {

    return namePrefixRepository.executeNameInfo(
        "2023-12-18",
        "2023-12-20",
        PageRequest.of(499, 100));
  }

  @Benchmark
  public Page<NamePrefixProjection> executeNameInfoLoopFirstPage() {

    return namePrefixRepository.executeNameInfoLoop(
        "2023-12-18",
        "2023-12-20",
        PageRequest.of(0, 100));
  }

  @Benchmark
  public Page<NamePrefixProjection> executeNameInfoLoop() {

    return namePrefixRepository.executeNameInfoLoop(
        "2023-12-18",
        "2023-12-20",
        PageRequest.of(499, 100));
  }

  @Benchmark
  public List<NamePrefixProjection> paginateNameInfoLoopFirstPage() {

    return namePrefixRepository.paginateNameInfoLoop(
        "2023-12-18",
        "2023-12-20",
        0,
        100);
  }

  @Benchmark
  public List<NamePrefixProjection> paginateNameInfoLoop() {

    return namePrefixRepository.paginateNameInfoLoop(
        "2023-12-18",
        "2023-12-20",
        49_900,
        50_000);
  }

}