package com.springboot.code.example.database.repository.testcase;

import java.math.BigDecimal;
import com.springboot.code.example.testcase.TestArguments;

public class NamePrefixRepositoryTestArguments {

  static TestArguments testExecuteNameInfoArguments;
  static TestArguments testBindParameterArguments = TestArguments
      .params(BigDecimal.TEN, 20);

  static {

    var firstPage = new NameInfoInput("2023-12-22", "2023-12-24", 0, 100);
    var specificPage = new NameInfoInput("2023-12-22", "2023-12-24", 499, 100);

    testExecuteNameInfoArguments = TestArguments
        .params(firstPage, 100)
        .nextParams(specificPage, 100);
  }

  public record NameInfoInput(String startDate, String toDate, int page, int size) {

  }

}