package com.springboot.code.example.database.repository.testcase;

import java.math.BigDecimal;
import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

public class NamePrefixRepositoryTestArguments {

  static List<TestArguments<NameInfoInput, Integer>> testExecuteNameInfoArguments;
  static List<TestArguments<BigDecimal, Integer>> testBindParameterArguments = List.of(
      TestArguments.of(BigDecimal.TEN, 20));

  static {

    var firstPage = new NameInfoInput("2023-12-22", "2023-12-24", 0, 100);
    var specificPage = new NameInfoInput("2023-12-22", "2023-12-24", 499, 100);

    testExecuteNameInfoArguments = List.of(
        TestArguments.of(firstPage, 100),
        TestArguments.of(specificPage, 100));
  }


  public record NameInfoInput(String startDate, String toDate, int page, int size) {

  }

}