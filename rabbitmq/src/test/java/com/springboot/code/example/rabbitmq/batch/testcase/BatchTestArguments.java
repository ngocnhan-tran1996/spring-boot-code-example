package com.springboot.code.example.rabbitmq.batch.testcase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import com.springboot.code.example.testcase.TestArguments;

class BatchTestArguments {

  static List<TestArguments<List<String>, Integer>> testBatchArguments;

  static {

    var evenMessage = new ArrayList<String>();
    var oddMessage = new ArrayList<String>();
    IntStream.range(1, 11)
        .forEach(i -> {

          String msg = new StringBuilder()
              .append("Even Msg ")
              .append(i)
              .toString();
          evenMessage.add(msg);
          oddMessage.add(msg);
        });

    oddMessage.add("Even Msg 11");

    testBatchArguments = List.of(
        TestArguments.of(evenMessage, 0),
        TestArguments.of(oddMessage, 1));
  }

}