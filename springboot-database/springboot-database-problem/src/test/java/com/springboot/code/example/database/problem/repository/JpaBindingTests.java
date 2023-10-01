package com.springboot.code.example.database.problem.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.code.example.database.problem.DatabaseIntegrationTests;
import com.springboot.code.example.database.problem.entity.CarEntity;

@SpringBootTest
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JpaBindingTests extends DatabaseIntegrationTests {

  @Autowired
  CarRepository carRepository;

  @Test
  void runBenchmarks() throws Exception {

    var options = new OptionsBuilder()
        .include(this.getClass().getSimpleName())
        .warmupIterations(1)
        .measurementIterations(3)
        .forks(1)
        .threads(1)
        .build();
    new Runner(options).run();
  }

  @Benchmark
  public List<CarEntity> bindNullParameter() {

    return carRepository.bindNullParameter(BigDecimal.ONE);
  }

  @Benchmark
  public List<CarEntity> bindParameter() {

    return carRepository.bindParameter(BigDecimal.ONE);
  }

  @Benchmark
  public List<CarEntity> nativeBindNullParameter() {

    return carRepository.nativeBindNullParameter(BigDecimal.ONE);
  }

  @Benchmark
  public List<CarEntity> nativeBindParameter() {

    return carRepository.nativeBindParameter(BigDecimal.ONE);
  }

}