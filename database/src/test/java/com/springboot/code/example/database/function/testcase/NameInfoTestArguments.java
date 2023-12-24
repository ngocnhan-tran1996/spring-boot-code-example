package com.springboot.code.example.database.function.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

public class NameInfoTestArguments {

  static List<TestArguments<NameInfoInput, Integer>> testExecuteNameInfoArguments;

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