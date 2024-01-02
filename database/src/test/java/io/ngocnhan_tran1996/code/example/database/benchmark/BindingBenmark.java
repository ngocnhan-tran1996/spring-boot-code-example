package io.ngocnhan_tran1996.code.example.database.benchmark;

import java.math.BigDecimal;
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
import io.ngocnhan_tran1996.code.example.database.domain.NamePrefixEntity;
import io.ngocnhan_tran1996.code.example.database.domain.NamePrefixRepository;

@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class BindingBenmark extends BenchmarkConfig {

  public static void main(String[] args) throws RunnerException {

    var opt = new OptionsBuilder()
        .include(BindingBenmark.class.getSimpleName())
        .warmupIterations(1)
        .measurementIterations(5)
        .forks(1)
        .warmupForks(1)
        .threads(1)
        .build();

    new Runner(opt).run();
  }

  @Autowired
  NamePrefixRepository namePrefixRepository;

  @Benchmark
  public List<NamePrefixEntity> bindNullParameter() {

    return namePrefixRepository.bindNullParameter(BigDecimal.TEN);
  }

  @Benchmark
  public List<NamePrefixEntity> bindParameter() {

    return namePrefixRepository.bindParameter(BigDecimal.TEN);
  }

  @Benchmark
  public List<Object[]> bindNullParameterNative() {

    return namePrefixRepository.bindNullParameterNative(BigDecimal.TEN);
  }

  @Benchmark
  public List<Object[]> bindParameterNative() {

    return namePrefixRepository.bindParameterNative(BigDecimal.TEN);
  }

}